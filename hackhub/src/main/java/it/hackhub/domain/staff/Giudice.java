package it.hackhub.domain.staff;

public class Giudice extends MembroStaff {

    // Aggiungi 'String password' qui sotto
    public Giudice(Long id, String nome, String email, String password) {
        super(id, nome, email, password); // Passala al padre
    }
}