//
//  ContactViewModel.swift
//  Contacts
//
//  Created by Răzvan Roşu on 13/02/24.
//  Copyright © 2024 tuist.io. All rights reserved.
//

import SwiftUI
import Repository

class ContactsViewModel : ObservableObject {
    
    @Published var sections = [ContactsSection]()
    
    private let repository: ContactsRepository
    
    init(repository: ContactsRepository) {
        self.repository = repository
    }
    
    func refresh() {
        sections = repository.get()
    }
    
    func get(id: ContactId) -> Contact {
        repository.get(id: id)
    }
    
    func add(new contact: NewContact) {
        repository.add(new: contact)
    }
    
    func update(contact: Contact) {
        repository.update(contact: contact)
    }
    
    func remove(id: ContactId) {
        repository.remove(id: id)
    }
}
