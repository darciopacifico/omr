<#macro comentarioClasse>Incluir arquivo com comentários</#macro>

<#-- IMPRIME COMENTÁRIO GERAL SOBRE A CLASSE E CONFIGURAÇÃO DO GERADOR NO MOMENTO DO PROCESSAMENTO -->
<#macro comentarioArquivoJava>
/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
</#macro>

<#-- Verifica se será necessário criar mecanismo de find. Se houver um método marcado como searchable==true, gera! 
Caso contrário, se-->
<#macro isCriarFind javaClass=domain, inverse=false>
	<#assign criar=false><#t>
	<@beanProperties ; p><#t>
		<#if isSearchable(p)><#t>
			<#assign criar=true><#return><#-- UM ATRIBUTO COM searchable=true é o suficiente--><#t>
		</#if><#t>
	</@beanProperties><#t>
	<#if (criar==true && inverse==false) || (criar==false && inverse==true)><#t>
			<#nested>
	</#if><#t>
</#macro>


<#-- Printa uma chamada ao JSF bean do módulo informado e concatena o elemento informado no argumento (atributo ou método) -->
<#macro beanElement elementName="", javaClass=domain ><#t>
${"#"+"{"+gc.getComponentVarName("JSFBean", javaClass)+"."+elementName}<#nested>${"}"}<#t>
</#macro>

<#-- Printa uma chamada ao JSF bean do módulo informado e concatena o elemento informado no argumento (atributo ou método) -->
<#function f_beanElement elementName="", javaClass=domain >
		<#return "#"+"{"+"gc.getComponentVarName('JSFBean', javaClass)"?eval+"."+elementName+"}" >
</#function>


<#-- Coloca qualquer literal entre braces -->
<#macro brc literal=""><#t>
${"#"+"{"}<#if literal!=""><#t>${literal}<#t>
<#else><#t>
	<#nested literal><#t>
</#if>${"}"}<#t>
</#macro>


<#-- printa argumentos de método de find-->
<#macro argumentosFind javaClass=domain, type=true><#t>
	<@iterateSearchableProperties ",", javaClass ; p, sufix><#t>
		<#if type=true>${genericReturnType(p)} </#if>${p.name}${sufix}<#t>
	</@iterateSearchableProperties ><#t>
</#macro>


<#-- Propriedades pesquisaveis (inclui relacionamentos) -->
<#macro propPesquisaveis javaClass=domain>
	<@iterateSearchableProperties "", javaClass ; p, sufix><#t>
	private ${genericReturnType(p)} ${p.name}${sufix};
	</@iterateSearchableProperties ><#t>
</#macro>

<#-- Relacionamento one -->
<#macro relacionamentoOne javaClass=domain, duplicate=true>
	<@iterateProperties "", javaClass, duplicate; p, suffix><#-- NAO PASSA O CONTROLE DE SEPARADOR PARA CIMA! CONTROLA LOCALMENTE! CASO CONTRARIO, PODE GERAR SEPARADORES A MAIS--><#t>
		<#if hasTagSC(p, "JazzProp","renderer")>
			<#if getAnnotationParam(p,"JazzProp","renderer").relacionamento=="UM"><#t>
				<#nested p, suffix><#t>
			</#if><#t>
		</#if><#t>
	</@iterateProperties>
</#macro>


<#-- Relacionamento Muitos -->
<#macro relacionamentoMany javaClass=domain>
	<@iterateSearchableProperties "", javaClass ; p, sufix><#t>
		<#if getAnnotationParam(p,"JazzProp","renderer").relacionamento=="MUITOS"><#t>
			<#nested p, sufix><#t>
		</#if><#t>
	</@iterateSearchableProperties ><#t>
</#macro>

<#-- DETERMINA O TIPO DO RETORNO CONTEMPLANDO GENERICS. EX: java.util.List<PessoaVO> -->
<#function genericReturnType property>
	<#assign returns = methodForProp(property).returns>
	<#assign strTypes="">

	<#-- O TIPO POSSUI TYPE ARGUMENTS "<X,Y,Z>" ??  -->
	<#if returns.actualTypeArguments??>
		<#assign typeArgs = returns.actualTypeArguments>
		<#if typeArgs?size gt 0>

			<#assign strTypes="<">
			<#list typeArgs as typeArg>
					<#assign strTypes=strTypes+typeArg.getJavaClass().getName()><#if typeArg_has_next>,</#if>
			</#list>
			<#assign strTypes=strTypes+">">

		</#if>
	</#if>

	<#-- CONCATENA NOME SIMPLES E, SE HOUVER, MAIS SEUS TYPE ARGUMENTS! -->
	<#return returns.getJavaClass().getName()+strTypes>
</#function>

<#-- Propriedades pesquisaveis Acessores -->
<#macro propPesquisaveisAccessors javaClass=domain>
	<@iterateSearchableProperties "", javaClass ; p, sufix><#t>
	/**
	 * Accessor Method
 	 * @return ${p.name}${sufix}
 	 */
	public ${genericReturnType(p)} ${p.accessor.name}${sufix}(){
		return this.${p.name}${sufix};
	}
	
	</@iterateSearchableProperties ><#t>
</#macro>


<#-- Propriedades pesquisaveis - Acessores -->
<#macro propPesquisaveisMutators javaClass=domain>
	<@iterateSearchableProperties "", javaClass ; p, sufix><#t>
	/**
	 * Mutator Method
 	 * @param ${p.name}${sufix}
 	 */
	public void ${p.mutator.name}${sufix}(${genericReturnType(p)} ${p.name}${sufix}){
		this.${p.name}${sufix} = ${p.name}${sufix};
	}
	
	</@iterateSearchableProperties ><#t>
</#macro>

<#-- 
Itera propriedades do bean tratando campos com comparacao por Range:
Se duplicate=true duplica atributos pesquisáveis com comparação do tipo RANGE. Ex: dtInclusao => dtInclusaoFrom e dtInclusaoTo 
-->
<#macro iterateProperties pSep="" javaClass=domain duplicate=true>
	<#assign sep=""><#t>
	<@beanProperties ; p><#t>
			<#if duplicate=true && hasTagSC(p, "JazzProp", "comparison", "RANGE")><#t>
				${sep}<#nested p "From"><#t>
				<#assign sep=pSep+""><#t>
				${sep}<#nested p "To"><#t>
			<#else><#t>
				${sep}<#nested p ""><#t>
			</#if><#t>
			<#assign sep=pSep+""><#t>
	</@beanProperties><#t>
</#macro>

<#-- Itera apenas argumentos pesquisáveis -->
<#macro iterateSearchableProperties pSeparator="" javaClass=domain duplicate=true>
	<#assign separator=""><#t>
	<@iterateProperties "", javaClass, duplicate; p, prefix><#-- NAO PASSA O CONTROLE DE SEPARADOR PARA CIMA! CONTROLA LOCALMENTE! CASO CONTRARIO, PODE GERAR SEPARADORES A MAIS--><#t>
		<#if isSearchable(p)><#t>
			${separator}<#nested p, prefix><#t>
			<#assign separator=pSeparator+" "><#t>
		</#if><#t>
	</@iterateProperties>
</#macro>

<#-- ignore,listable,readOnly,searchable,instantSearch,sortable,visible,updatable-->

<#-- Itera apenas argumentos pesquisáveis -->
<#macro iterateSearchableProperties pSeparator="" javaClass=domain duplicate=true>
	<#assign separator=""><#t>

	<@iterateProperties "", javaClass, duplicate; p, prefix><#-- NAO PASSA O CONTROLE DE SEPARADOR PARA CIMA! CONTROLA LOCALMENTE! CASO CONTRARIO, PODE GERAR SEPARADORES A MAIS--><#t>
		<#if isSearchable(p)><#t>
			${separator}<#nested p, prefix><#t>
			<#assign separator=pSeparator+" "><#t>
		</#if><#t>
	</@iterateProperties>
</#macro>

<#--TESTA SE ATRIBUTO E PESQUISAVEL OU NAO-->
<#function isSearchable p>
	<#return hasTagSC(p, "JazzProp", "searchable", "true")>
</#function>


<#-- printa comentários para metodo find-->
<#macro comentariosFind javaClass=domain>
	/**
	 * Pesquisa entidades do tipo ${javaClass.name} 
	 * @author ${env.USER}
	<@iterateSearchableProperties "", javaClass ; p, sufix><#t>
	 * @param ${p.name}${sufix}
	</@iterateSearchableProperties ><#t>
	<#nested>
	 * @returns Coleção de ${javaClass.name}
	 */
</#macro>


<#-- determina o nome do metodo de find -->
<#macro metodoFind ordered=false, javaClass=domain>
	<@gc.isCriarFind><#t>
	find${javaClass.name}<#t>
	</@gc.isCriarFind><#t>
</#macro>

<#-- determina o nome do metodo de count -->
<#macro metodoCount ordered=false, javaClass=domain>
	<@gc.isCriarFind><#t>
	count${javaClass.name}<#t>
	</@gc.isCriarFind><#t>
</#macro>


<#-- determina o nome do metodo de count para paginacao -->
<#macro metodoCount ordered=false, javaClass=domain>
	<@gc.isCriarFind><#t>
	count${javaClass.name}<#t>
	</@gc.isCriarFind><#t>
</#macro>

<#-- determina o nome do metodo de find -->
<#macro findReturns javaClass=domain>List<${javaClass.name}></#macro>

<#-- retorna o metodo para a propriedade informada, priorizando os getters -->
<#function methodForProp prop>
	<#if prop.accessor?? >
		<#return prop.accessor >
	<#elseif prop.mutator?? >
		<#return prop.mutator >
	</#if>
</#function>


<#-- Testa XDoclet de uma propriedade -->
<#function hasTagSC qdoxElement, tagName, attributeName="attrib_def_uhyg", value="vlPadrao_XXX">
	<#-- chama hasTag passando superclasses=true -->
	<#return hasTag(qdoxElement, tagName, true, attributeName, value)>
</#function>



<#-- Testa XDoclet de um qdoxElement, podendo ser um JavaClass, JavaMethod ou BeanProperty -->
<#function hasTag qdoxElement, tagName, superclasses=true, attributeName="attrib_def_uhyg", value="vlPadrao_XXX">

	<#assign annotation=getAnnotation(qdoxElement, tagName)>
	 
	<#if attributeName=="attrib_def_uhyg">
		<#return annotation?? && annotation!=""><#--apenas testa se possui a annotation informada-->
	<#else>
		<#if value=="vlPadrao_XXX">
			<#return  	annotation?? && annotation!="" && 
						annotation.getNamedParameter(attributeName)??><#--Possuo tag e atributo de tag informado (nao checa valor do atributo||)-->
		<#else>		
			<#return 	annotation?? && annotation!="" &&
						annotation.getNamedParameter(attributeName)?? && 
						annotation.getNamedParameter(attributeName)==value><#--Possuo tag, atributo de tag informado e valor igual ao informado-->
		</#if>
	</#if>
</#function>

<#-- Recupera Annotacao solicitada -->
<#function getAnnotation qdoxElement, tagName>

	<#if qdoxElement.class.name="com.thoughtworks.qdox.model.BeanProperty">
		<#assign taggable=methodForProp(qdoxElement) ><#-- É UM PROPERTIE. RECUPERANDO MÉTODO CORRESPONTENDE -->
	<#else>
		<#assign taggable=qdoxElement ><#--É uma implementação de qdoxElement já com suporte a tags xdoclet-->
	</#if>

	<#list taggable.getAnnotations() as a>
		<#if a.getType().getJavaClass().getName()==tagName>
			<#return a>
		</#if>
	</#list>
	
	<#return "">
</#function>


<#-- Recupera parametro da Annotacao solicitada -->
<#function getAnnotationParam qdoxElement, ptagName, parameter>
	
	<#-- RESOLVE ALGUM ALIAS PARA UMA TAGLIBRARY, EX: "JazzProp" é alias para "br.com.jazz.codegen.annotation.JazzProp" -->
	<#if aliasdlp[ptagName]??>
		<#-- OK, encontrou o alias e vai substituir, por exemplo, "JazzProp" por "br.com.jazz.codegen.annotation.JazzProp" -->
		<#assign tagName=aliasdlp[ptagName]/>
	<#else>
		<#-- Nao encontrou alias. Irá manter a string informada. -->
		<#assign tagName=ptagName/>
	</#if>
	
	<#return modelLoader.getAnnotationValue(qdoxElement, tagName, parameter) />
</#function>


<#-- Recupera parametro da Annotacao solicitada -->
<#function getAnnotationParam2 qdoxElement, ptagName>
	
	<#-- RESOLVE ALGUM ALIAS PARA UMA TAGLIBRARY, EX: "JazzProp" é alias para "br.com.jazz.codegen.annotation.JazzProp" -->
	<#if aliasdlp[ptagName]??>
		<#-- OK, encontrou o alias e vai substituir, por exemplo, "JazzProp" por "br.com.jazz.codegen.annotation.JazzProp" -->
		<#assign tagName=aliasdlp[ptagName]/>
	<#else>
		<#-- Nao encontrou alias. Irá manter a string informada. -->
		<#assign tagName=ptagName/>
	</#if>
	
	<#return modelLoader.getAnnotationValue(qdoxElement, tagName) />
</#function>



<#-- Recupera parametro da Annotacao solicitada -->
<#function getBooleanParam qdoxElement, ptagName, parameter>
	
	<#-- RESOLVE ALGUM ALIAS PARA UMA TAGLIBRARY, EX: "JazzProp" é alias para "br.com.jazz.codegen.annotation.JazzProp" -->
	<#if aliasdlp[ptagName]??>
		<#-- OK, encontrou o alias e vai substituir, por exemplo, "JazzProp" por "br.com.jazz.codegen.annotation.JazzProp" -->
		<#assign tagName=aliasdlp[ptagName]/>
	<#else>
		<#-- Nao encontrou alias. Irá manter a string informada. -->
		<#assign tagName=ptagName/>
	</#if>
	
	<#return modelLoader.getBooleanValue(qdoxElement, tagName, parameter) />
</#function>



<#-- DETERMINA QUAL SERÁ O RENDERIZADOR PARA A PROPRIEDADE -->
<#function getRendererProperty qdoxElement>

	<#assign render=gc.getAnnotationParam(qdoxElement,"JazzProp", "renderer")/>
		
	<#if render!="DEFAULT">
		<#return render>
	</#if>
	
	
	
	<#return "TEXT">
</#function>


<#-- IMPRIME SEQUENCIA DE MODIFICADORES DO VO ORIGINAL -->
<#macro modificadores javaClass=domain>
	<#list javaClass.modifiers as m >${m} </#list><#t>
</#macro>


<#-- IMPRIME PROPERTIES ENCONTRADOS -->
<#macro beanProperties pJavaClass=domain, superclasses=true>
	<#if pJavaClass.class.name=="com.thoughtworks.qdox.model.Type">
		<#assign javaClass=pJavaClass.javaClass><#-- É UM TYPE: RECUPERAR O JAVACLASS -->
	<#elseif pJavaClass.class.name=="com.thoughtworks.qdox.model.JavaClass">
		<#assign javaClass=pJavaClass><#-- É UM JavaClass: manter o próprio valor -->
	</#if>
	<#list javaClass.getBeanProperties(superclasses) as p>
		<#if !hasTagSC(p, "JazzProp", "ignore", "true")><#t>
		<#nested p><#-- Chama conteudo nested -->
		</#if><#t>
	</#list>
</#macro>

<#-- LER PROPRIEDADES CARREGADAS VIA APPLICATIONCONTEXT.XML para o template -->
<#function properties value>
	<#if rules.properties[value]??>
		<#return rules.properties[value]>
	<#else>
		<#return "">
	</#if>
</#function>


<#-- printa hierarquia de Extensão ou Implementação (type= extends ou implements) -->
<#-- ex: public class TipoRequisitoBusinessImpl extends AbstractBusinessImpl<TipoRequisitoPK,TipoRequisitoVO> implements TipoRequisitoBusiness  -->
<#--                                                    ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^            ^^^^^^^^^^^^^^^^^^^^^  -->
<#macro printHierarchy type append="" >
	<#assign hierarchyConfig = properties(type)> <#-- recupera propriedade  das configurações -->
	<#if (hierarchyConfig?? && hierarchyConfig!="") || (append?? && append!="") > ${type}</#if><#t><#-- If para determinar se escreve "implemets" ou "extends" -->
	<#if append?? && append!=""> ${append}</#if><#t><#-- concatena personalizado para extends ou implements ex: implements Serializable, I -->
	<#if hierarchyConfig?? && hierarchyConfig!="" && append?? && append!="">,</#if><#t><#-- Determina se será necesário colocar uma vírgula ex: implements Serializable, Comparator {... -->
	<#if hierarchyConfig?? && hierarchyConfig!=""> ${hierarchyConfig}<@printParameterTypes param="extendsParamTypes"/></#if><#t>
</#macro>


<#-- printa os parameter types para declaracoes de hierarquias genericas de objetos-->
<#-- ex: public class TipoRequisitoBusinessImpl extends AbstractBusinessImpl<TipoRequisitoPK,TipoRequisitoVO> implements TipoRequisitoBusiness  -->
<#--                                                                        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^                                   -->
<#macro printParameterTypes param>
	<#assign paramTypes=properties(param)>
	<#if paramTypes?? && paramTypes!="" ><#t>
		<<#list paramTypes?split(",") as metaType><#t>
		<@gc.componentName "${metaType}" /><#if metaType_has_next>,</#if><#t>
		</#list>><#t>
	</#if><#t>
</#macro>


<#-- IMPRIME UMA ASSINATURA DE CLASSE -->
<#-- ex: public class TipoRequisitoBusinessImpl extends AbstractBusinessImpl<TipoRequisitoPK,TipoRequisitoVO> implements TipoRequisitoBusiness  -->
<#--     ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^  -->
<#macro classSignature type="class" extends="" implements="" >
	<@gc.modificadores/>${type} <@componentName/><#t>
	<@printHierarchy type="extends" 		append=extends		/><#t>
	<@printHierarchy type="implements"	append=implements	/><#t>
</#macro> 


<#-- DETERMINA O NOME DO COMPONENTE PARA O TEMPLATE E A JavaClass informada. -->
<#function getComponentName templateName=rulesName, javaClass=domain>
	<#return rulesFactory.getTemplateRules(templateName).getJavaComponentName(javaClass)><#t>
</#function>



<#-- DETERMINA A DECLARACAO DO COMPONENTE, CONTEMPLANDO O TYPEPARAMETERS PARA GENERICS -->
<#function getComponentGenericName templateName=rulesName, javaClass=domain>
	<#return rulesFactory.getTemplateRules(templateName).getJavaComponentGenericName(javaClass)><#t>
</#function>

<#-- DETERMINA O NOME DE VARIAVEL PARA O COMPONENTE E A JavaClass informada. -->
<#function getComponentVarName templateName=rulesName, javaClass=domain>
	<#return getComponentName(templateName, javaClass)?uncap_first><#t>
</#function>

<#-- DETERMINA O NOME DO COMPONENTE PARA O TEMPLATE E A JavaClass informada. -->
<#macro componentName templateName=rulesName, javaClass=domain>
	${getComponentName(templateName, javaClass)}<#t>
</#macro>

<#-- DETERMINA O NOME DO COMPONENTE PARA A JAVA CLASS -->
<#macro componentVarName templateName=rulesName, javaClass=domain>
	${getComponentVarName(templateName, javaClass)}<#t>
</#macro>

<#-- 
	TESTA SE O DOMAIN REPRESENTA UM MÓDULO PRIMÁRIO E MODULO PAI NO SISTEMA.
	Ex:
		PedidoAbstratoVO = ABSTRATO						(gero DAOAbstrato, mas não gero tela) 
		PedidoVendaVO= MODULO PAI E CONCRETO		(gero DAO e tela)
		PedidoCompraVO= MODULO PAI E CONCRETO		(gero DAO e tela)
		ItemPedidoVendaVO = MODULO FILHO DE PedidoVendaVO e concreto (Não gero DAO nem tela, se item de pedido for um grid na tela de pedidoVenda)
 -->
<#function isParentConcrete domain>
	 <#return !domain.isAbstract() && !domain.isInterface()>
</#function>