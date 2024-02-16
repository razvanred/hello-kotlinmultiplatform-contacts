//
//  ContactDetailView.swift
//  Contacts
//
//  Created by Răzvan Roşu on 13/02/24.
//  Copyright © 2024 tuist.io. All rights reserved.
//

import SwiftUI
import RepositoryKt

struct ContactDetailView : View {
    let viewModel: ContactsViewModel
    let contact: Contact
    
    @Environment(\.presentationMode)
    var presentationMode: Binding<PresentationMode>
    
    @State
    private var isEditContactNameDialogPresented: Bool = false
    
    @State
    private var isDeleteAlertPresented: Bool = false
    
    @State
    private var editContactNameDialogValue: String = ""
    
    var body: some View {
        List {
            Button(
                action: {
                    editContactNameDialogValue = contact.name
                    isEditContactNameDialogPresented = true
                },
                label: {
                    HStack {
                        Image(systemName: "pencil")
                        Text(String(localized: "Edit button"))
                    }
                }
            )
                .alert(
                    String(localized: "Edit contact dialog title"),
                    isPresented: $isEditContactNameDialogPresented,
                    actions: {
                        TextField(String(localized: "Contact name field name"), text: $editContactNameDialogValue)
                        
                        //let name = editContactNameDialogValue.trimmingCharacters(in: .whitespacesAndNewlines)
                        
                        Button(String(localized: "Edit button"), action: {
                            // viewModel.update(contact: Contact(id: contact.id, name: name))
                            viewModel.update(contact: Contact(id: contact.id, name: editContactNameDialogValue.trimmingCharacters(in: .whitespacesAndNewlines)))
                            viewModel.refresh()
                            isEditContactNameDialogPresented = false
                        })
                            //.disabled(name.isEmpty)
                        
                        Button(String(localized: "Cancel dialog button"), role: .cancel, action: {
                            isEditContactNameDialogPresented = false
                        })
                    }
                )
            Button(
                role: .destructive,
                action: {
                    isDeleteAlertPresented = true
                }, 
                label: {
                    HStack {
                        Image(systemName: "trash")
                        Text(String(localized: "Delete button"))
                    }
                }
            )
                .alert(isPresented: $isDeleteAlertPresented) {
                    Alert(
                        title: Text(String(format: String(localized: "Delete contact dialog title"), contact.name)),
                        message: Text(String(localized: "Delete contact dialog message")),
                        primaryButton: .destructive(Text(String(localized: "Delete button"))) {
                            viewModel.remove(id: contact.id)
                            isDeleteAlertPresented = false
                            viewModel.refresh()
                            presentationMode.wrappedValue.dismiss()
                        },
                        secondaryButton: .cancel {
                            isDeleteAlertPresented = false
                        }
                    )
                }
        }
        .navigationTitle(contact.name)
    }
}
