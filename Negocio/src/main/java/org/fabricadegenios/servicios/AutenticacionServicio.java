package org.fabricadegenios.servicios;

import org.fabricadegenios.dto.LoginDTO;
import org.fabricadegenios.dto.TokenDTO;

public interface AutenticacionServicio {

    TokenDTO login(LoginDTO loginDTO) throws Exception;
}
