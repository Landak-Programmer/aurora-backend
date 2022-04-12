package com.aurora.backend.object.vc;

import com.aurora.backend.entity.Contact;
import com.aurora.backend.exception.VCProcessorException;
import com.aurora.backend.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class VCWhatsappProcessor extends IVCProcessor {

    @Autowired
    private ContactService contactService;

    @Override
    public String idArgs() {
        return "SEND_WHATSAPP";
    }

    @Override
    protected List<String> limiter() {
        final List<String> baseLimiter = super.limiter();
        baseLimiter.add("message");
        return baseLimiter;
    }

    @Override
    public String internal1(final String args) {

        Contact contact = contactService.getContactByName(args);
        if (contact != null) {
            return contact.getPhoneNumber();
        }
        // do fuzzy search
        contact = fuzzySearch(args);

        if (contact != null) {
            return contact.getPhoneNumber();
        }
        throw new VCProcessorException("Unable to find contact from name " + args);
    }

    /**
     * Use elastic search or some intent, big data etc. this is naive
     */
    private Contact fuzzySearch(final String args) {
        final Set<Contact> set = new HashSet<>();
        for (final String s : args.split("\\s+")) {
            final List<Contact> startWith = contactService.getContactByNameStartWith(s);
            final List<Contact> endWith = contactService.getContactByNameEndsWith(s);
            final List<Contact> containing = contactService.getContactByNameContaining(s);
            set.addAll(startWith);
            set.addAll(endWith);
            set.addAll(containing);
        }

        if (set.isEmpty())
            return null;

        final Iterator<Contact> iterator = set.iterator();
        // fixme: don't get 1st one, do more logic instead!!!
        return iterator.next();
    }

    public String internal2(final String args) {
        return args;
    }
}
