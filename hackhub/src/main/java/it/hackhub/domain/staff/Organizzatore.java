package it.hackhub.domain.staff;

public class Organizzatore extends MembroStaff {

    // Aggiungi 'String password' qui sotto
    public Organizzatore(Long id, String nome, String email, String password) {
        super(id, nome, email, password); // Passala al padre
    }
}