" select "+
" 1 	eve_pk, "+
" 1 	par_pk, "+
" 1	pes_pk, "+
" 1 	pa_pk, "+
" 'Nome de Evento Chumbado na Query do relatorio' e_description, "+
" 'dlopespacifico' pe_login, "+
" 'Darcio Mais Alguma Coisa Lopes Pacifico' pe_nome, "+
" qtn.pk	qtn_pk, "+
" qco.question_order, "+
" aco.alternative_order, "+
" qco.pk	qco_pk, "+
" aco.pk	aco_pk, "+
" exv.pk	exv_pk, "+
" qco.pk	qco_pk, "+
" que.pk	que_pk, "+
" alt.pk	alt_pk, "+
" exv.pk exv_pk, "+
" que.pk q_pk, "+
" alt.pk a_pk, "+
" exv.question_enum_model, "+
" exv.alternative_enum_model, "+
" que.desc_wiki q_wiki, "+
" que.question_type, "+
" que.verbosity q_verbosity, "+
" qco.pk qc_pk, "+
" qtn.pk qr_pk, "+
" qtn.description qr_description, "+
" alt.desc_wiki a_wiki, "+
" alt.resumo a_resumo, "+
" alt.verbosity a_verbosity, "+
" aco.pk ac_pk "+
" from tb_exam_variant exv "+
" 	inner join tb_question_coordinate qco		on qco.fk_exam_variant		= exv.pk "+
" 	inner join tb_question que 				on que.pk 					= qco.fk_question "+
" 	inner join tj_questionnairevo_questions	jqq 	on jqq.fk_questions_question	= que.pk "+
" 	inner join tb_questionnaire qtn			on qtn.pk					= jqq.fk_questionnaire "+
" 	inner join tb_alternative_coordinate aco	on aco.fk_question_coordinates = qco.pk "+
" 	inner join tb_alternative alt				on alt.pk 					= aco.fk_alternative "+
" where exv.pk=1 "+
" order by "+
" qtn.pk	, "+
" qco.question_order, "+
" aco.alternative_order ";
