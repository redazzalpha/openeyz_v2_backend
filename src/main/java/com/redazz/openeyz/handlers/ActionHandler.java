/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.handlers;

import com.redazz.openeyz.Exceptions.DataNotFoundException;
import com.redazz.openeyz.Exceptions.UnauthorizedException;
import com.redazz.openeyz.classes.PublicationComponent;
import com.redazz.openeyz.models.Users;
import com.redazz.openeyz.services.UserService;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 *
 * @author kyzer
 */
@Component
public class ActionHandler {

    @Autowired
    UserService us;

    public <T extends PublicationComponent> ResponseEntity<String> run(String currentUserId, T publicationComponent, Function<Long, Void> callback) {
        Users currentUser = null;
        try {
            currentUser = us.findById(currentUserId).get();

            if (isAuthorized(currentUser, publicationComponent)) {
                callback.apply(publicationComponent.getId());
            }
            else {
                throw new UnauthorizedException("User is not authorized to do this action");
            }
        }
        catch (RuntimeException e) {
            if (currentUser == null) {
                try {
                    throw new DataNotFoundException("user was not found in database");
                }
                catch (DataNotFoundException ex) {
                    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
                }
            }
            try {
                throw new DataNotFoundException(e.getMessage());
            }
            catch (DataNotFoundException ex) {
                return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
            }
        }
        catch (UnauthorizedException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
        }
        catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("action handler success", HttpStatus.OK);
    }
    private <T extends PublicationComponent> boolean isAuthorized(Users currentUser, T publicationComponent) {
        String currentUserRole = currentUser.getRoles().get(0).getRoleName().toString();
        String publicationComponentAuthor = publicationComponent.getAuthor().getUsername();

        boolean isSupervisor = currentUserRole.equals("SUPERADMIN") || currentUserRole.equals("ADMIN");
        boolean isOwner = currentUser.getUsername().equals(publicationComponentAuthor);

        return  isSupervisor || isOwner;
    }
}
