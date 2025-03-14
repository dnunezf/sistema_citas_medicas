package org.example.sistema_citas_medicas.logica.mappers;

public interface Mapper<A, B>{
    B mapTo(A a);
    A mapFrom(B b);
}
