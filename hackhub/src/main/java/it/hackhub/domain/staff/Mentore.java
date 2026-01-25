package it.hackhub.domain.staff;

public class Mentore extends MembroStaff {

    // Aggiungi 'String password' qui sotto
    public Mentore(Long id, String nome, String email, String password) {
        super(id, nome, email, password); // Passala al padre
    }
}