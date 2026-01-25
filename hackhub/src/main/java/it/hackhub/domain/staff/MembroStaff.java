package it.hackhub.domain.staff;
import it.hackhub.domain.Utente;

public abstract class MembroStaff extends Utente {


    public MembroStaff(Long id, String nome, String email, String password) {
        super(id, nome, email, password);
    }

}