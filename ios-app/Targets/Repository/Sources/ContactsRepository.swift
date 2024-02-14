//
//  ContactsRepository.swift
//  Contacts
//
//  Created by Răzvan Roşu on 13/02/24.
//  Copyright © 2024 tuist.io. All rights reserved.
//

public final class ContactsRepository {
    private var contacts: [ContactId : Contact] = mockContacts
        .map { contact in
            (contact.id, contact)
        }
        .reduce(into: [:]) { dictionary, tuple in
            dictionary[tuple.0] = tuple.1
        }
    
    public init() { }
    
    public func add(new contact: NewContact) {
        let id = ContactId()
        contacts[id] = Contact(id: id, name: contact.name)
    }
    
    public func update(contact: Contact) {
        contacts[contact.id] = contact
    }
    
    public func remove(id: ContactId) {
        contacts.removeValue(forKey: id)
    }
    
    public func get() -> [(Character, [Contact])] {
        Dictionary(
            grouping: contacts.values.sorted(by: { $0.name < $1.name }),
            by: { $0.name.first! }
        )
            .sorted(by: { $0.key < $1.key })
    }
    
    public func get(id: ContactId) -> Contact {
        contacts[id]!
    }
}
