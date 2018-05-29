package com.example.easycontacts.example;

import com.example.easycontacts.model.Address;
import com.example.easycontacts.model.Contact;
import com.example.easycontacts.model.Email;
import com.example.easycontacts.model.Phone;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xkrej63 on 29.05.2018.
 */

public class Contacts {

    public static final List<Contact> contacts = new ArrayList<Contact>() {{
        add(new Contact(
                "exapmle-uuid-1",
                "Jan",
                "Novak",
                new ArrayList<Email>() {{
                    add(new Email("personal", "jan.novak@seznam.cz"));
                    add(new Email("work", "jan.novak@work.cz"));
                }},
                new ArrayList<Address>() {{
                    add(new Address("home", "nám. Winstona Churchilla 1938/4, 130 67 Praha 3"));
                }},
                new ArrayList<Phone>() {{
                    add(new Phone("personal", "+420603414256"));
                }},
                "best friend",
                "Best company inc."
        ));
        add(new Contact(
                "exapmle-uuid-1",
                "Helena",
                "Novakova",
                new ArrayList<Email>() {{
                    add(new Email("personal", "helca.novakova@gmail.com"));
                }},
                null,
                new ArrayList<Phone>() {{
                    add(new Phone("personal", "+420608254365"));
                    add(new Phone("work", "+420777254125"));
                }},
                "best friend\nforever",
                "Best company inc."
        ));
    }};
}
