package com.msaf.validador.integration.util;

public interface IFilter<T> {
	boolean aceita(T value);
}

/*
IFilter<String> FILTRO_APENAS_COMECAM_COM_A = new IFilter<String>() {
		public boolean aceita(String value) {
			return value != null && value.toLowerCase().startsWith("a");
		}
	};
	
List<Lista> listaBaguncada = CollectionsUtils.filter(new IFilter<Lista>(){
			public boolean aceita(Lista value) {
				return value.getCodGrupo().equals(grupoLista);
			}
		}, listas);
		
		new IFilter<Template>(){
			public boolean aceita(Template template) {
				return template.getTipo() == ITipoTemplate.DETALHE;
			}

		}
*/