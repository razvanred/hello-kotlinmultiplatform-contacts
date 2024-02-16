//
//  Models.swift
//  Contacts
//
//  Created by Răzvan Roşu on 13/02/24.
//  Copyright © 2024 tuist.io. All rights reserved.
//

import Foundation

public struct ContactId : Hashable {
    public let value: String
    
    public init() {
        value = UUID().uuidString
    }
    
    public init(_ value: String) {
        self.value = value
    }
}

public struct Contact : Identifiable {
    public let id: ContactId
    public let name: String
    
    public init(name: String) {
        self.init(id: ContactId(), name: name)
    }
    
    public init(id: ContactId, name: String) {
        self.id = id
        if name.isEmpty {
            fatalError("The name must be not empty")
        }
        self.name = name
    }
}

public struct ContactsSection {
    public let initial: Character
    public let contacts: [Contact]
}

public struct NewContact {
    public let name: String
    
    public init(name: String) {
        if name.isEmpty {
            fatalError("The name must be not empty")
        }
        self.name = name
    }
}
