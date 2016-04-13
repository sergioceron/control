package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.Announcement;
import mx.ipn.cidetec.virtual.entities.User;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;

import javax.persistence.EntityManager;

/**
 * Created by Usuario on 28/09/2014.
 */
@Name( "announcementController" )
@Scope( ScopeType.CONVERSATION )
public class AnnouncementController {

    private Announcement announcement = new Announcement();
    private boolean removable = true;

    @In
    private Identity identity;

    @In
    private EntityManager entityManager;

    @Logger
    private Log log;

    @End
    public String save() {
        User current = entityManager.find( User.class, identity.getCredentials().getUsername() );
        announcement.setAutor( current );
        entityManager.persist( announcement );
        entityManager.flush();
        return "success";
    }

    public void prepareToRemove( Announcement announcement ) {
        this.announcement = announcement;
    }

    public void remove() {
        entityManager.remove( announcement );
        entityManager.flush();
    }

    public Announcement getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement( Announcement announcement ) {
        this.announcement = announcement;
    }

    public boolean isRemovable() {
        return removable;
    }

    public void setRemovable( boolean removable ) {
        this.removable = removable;
    }

}
