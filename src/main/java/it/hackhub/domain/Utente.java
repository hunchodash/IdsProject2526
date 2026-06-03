package it.hackhub.domain;

public class Utente {
    private Long id;
    private String nome;
    private String email;
    private String password;

    public Utente(Long id, String nome, String email, String password) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.password = password;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
}
