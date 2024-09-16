package org.fabricadegenios.dto;

import org.fabricadegenios.model.Usuario;

public record LoginDTO (String email, String password, Usuario usuario){

}
