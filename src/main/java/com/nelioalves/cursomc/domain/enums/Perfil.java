package com.nelioalves.cursomc.domain.enums;

public enum Perfil {
	
	//O PREFIXO "ROLE_" É EXIGÊNCIA DO SPRINT SECURITY, PARA O CONTROLE DE SEGURANÇA
	ADMIN(1, "ROLE_ADMIN"),
	CLIENTE(2, "ROLE_CLIENTE");
	
	private int codigo;
	private String descricao;
	
	private Perfil(int codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public int getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static Perfil toEnum(Integer codigo) {
		if (codigo == null) {
			return null;
		}
		
		for (Perfil x : Perfil.values()) {
			if (codigo.equals(x.getCodigo())) {
				return x;
			}			
		}
		
		throw new IllegalArgumentException("Id inválido: " + codigo);
	}

}
