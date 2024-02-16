//
//  ContactsRepository.swift
//  Contacts
//
//  Created by Răzvan Roşu on 13/02/24.
//  Copyright © 2024 tuist.io. All rights reserved.
//

public final class ContactsRepository {
    private var sections: [ContactId : Contact] = mockContacts
        .map { contact in
            (contact.id, contact)
        }
        .reduce(into: [:]) { dictionary, tuple in
            dictionary[tuple.0] = tuple.1
        }
    
    public init() { }
    
    public func add(new contact: NewContact) {
        let id = ContactId()
        sections[id] = Contact(id: id, name: contact.name)
    }
    
    public func update(contact: Contact) {
        sections[contact.id] = contact
    }
    
    public func remove(id: ContactId) {
        sections.removeValue(forKey: id)
    }
    
    public func get() -> [ContactsSection] {
        Dictionary(
            grouping: sections.values.sorted(by: { $0.name < $1.name }),
            by: { $0.name.first! }
        )
            .map { initial, contacts in
                ContactsSection(initial: initial, contacts: contacts)
            }
            .sorted(by: { $0.initial < $1.initial })
    }
    
    public func get(id: ContactId) -> Contact {
        sections[id]!
    }
}
