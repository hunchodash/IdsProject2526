package it.hackhub.domain;

public class Utente {
    private Long id;
    private String nome;
    private String email;
    private String password;

    public Utente(Long id, String nome, String email, String password) {
        if (nome == null || nome.isBlank() || email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Nome, email e password sono obbligatori");
        }
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.password = password;
    }

    public void aggiornaProfilo(String nuovoNome, String nuovaEmail) {
        if (nuovoNome == null || nuovoNome.isBlank()) {
            throw new IllegalArgumentException("Nome obbligatorio");
        }
        if (nuovaEmail == null || nuovaEmail.isBlank()) {
            throw new IllegalArgumentException("Email obbligatoria");
        }
        this.nome = nuovoNome;
        this.email = nuovaEmail;
    }

    public String getDatiBase() {
        return nome + " - " + email + " - " + getClass().getSimpleName();
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
}
