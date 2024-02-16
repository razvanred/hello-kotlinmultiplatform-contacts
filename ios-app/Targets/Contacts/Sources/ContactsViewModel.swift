//
//  ContactViewModel.swift
//  Contacts
//
//  Created by Răzvan Roşu on 13/02/24.
//  Copyright © 2024 tuist.io. All rights reserved.
//

import SwiftUI
import RepositoryKt

class ContactsViewModel : ObservableObject {
    
    @Published var sections = [ContactsSection]()
    
    private let repository: ContactsRepository
    
    init(repository: ContactsRepository) {
        self.repository = repository
    }
    
    func refresh() {
        sections = repository.getAll()
    }
    
    func get(id: ContactId) -> Contact {
        repository.getById(id: id)
    }
    
    func add(new contact: NewContact) {
        repository.add(newContact: contact)
    }
    
    func update(contact: Contact) {
        repository.update(contact: contact)
    }
    
    func remove(id: ContactId) {
        repository.removeById(id: id)
    }
}
