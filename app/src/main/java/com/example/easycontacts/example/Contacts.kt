package com.example.easycontacts.example

import com.example.easycontacts.model.api.Address
import com.example.easycontacts.model.api.Contact
import com.example.easycontacts.model.api.Email
import com.example.easycontacts.model.api.Phone

import java.util.ArrayList

/**
 * Created by xkrej63 on 29.05.2018.
 */

object Contacts {

    val contacts: List<Contact> = object : ArrayList<Contact>() {
        init {
            add(Contact(
                    "exapmle-uuid-1",
                    "Jan",
                    "Novak",
                    object : ArrayList<Email>() {
                        init {
                            add(Email("personal", "jan.novak@seznam.cz"))
                            add(Email("work", "jan.novak@work.cz"))
                        }
                    },
                    object : ArrayList<Address>() {
                        init {
                            add(Address("home", "nám. Winstona Churchilla 1938/4, 130 67 Praha 3"))
                        }
                    },
                    object : ArrayList<Phone>() {
                        init {
                            add(Phone("personal", "+420603414256"))
                        }
                    },
                    "best friend",
                    "Best company inc."
            ))
            add(Contact(
                    "exapmle-uuid-1",
                    "Helena",
                    "Novakova",
                    object : ArrayList<Email>() {
                        init {
                            add(Email("personal", "helca.novakova@gmail.com"))
                        }
                    },
                    listOf(),
                    object : ArrayList<Phone>() {
                        init {
                            add(Phone("personal", "+420608254365"))
                            add(Phone("work", "+420777254125"))
                        }
                    },
                    "best friend\nforever",
                    "Best company inc."
            ))
        }
    }
}
