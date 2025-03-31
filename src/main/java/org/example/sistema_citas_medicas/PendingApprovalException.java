package org.example.sistema_citas_medicas;



import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class PendingApprovalException extends UsernameNotFoundException {
    public PendingApprovalException(String msg) {
        super(msg);
    }
}
