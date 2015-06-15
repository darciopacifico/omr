select 
ei.pk exam_pk,
ei.description exam_desc,
ei.dt_creation,
ei.exam_model,
ei.fk_aplicador_pessoa,
ei.fk_avaliado_pessoa,
ei.fk_criador_pessoa,

qi.pk question_pK,
qi.description_wiki,
qi.question_type,
qi.fk_exam,

ai.pk alternative_pk,
ai.alternative_order,
ai.description alternative_desc,
ai.resumo,
ai.score,
ai.fk_question,

app.nome aplicador,
avp.nome avaliador,
crp.nome criador


from tb_exam_instance ei
	inner join tb_question_instance qi on qi.fk_exam = ei.pk
	inner join tb_alternative_instance ai on ai.fk_question = qi.pk

	inner join tb_pessoa avp on avp.pk = ei.fk_avaliado_pessoa
	inner join tb_pessoa app on app.pk = ei.fk_avaliado_pessoa
	inner join tb_pessoa crp on crp.pk = ei.fk_avaliado_pessoa

where ei.pk = 1
