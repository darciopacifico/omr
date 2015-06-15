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
		<#if hasTagSC(p, "JazzProp", "searchable", "true")><#t>
			<#assign criar=true><#return><#-- UM ATRIBUTO COM searchable=true é o suficiente--><#t>
		</#if><#t>
	</@beanProperties><#t>
	<#if (criar==true && inverse==false) || (criar==false && inverse==true)><#t>
			<#nested>
	</#if><#t>
</#macro>


<#-- Printa uma chamada ao JSF bean do módulo informado e concatena o elemento informado no argumento (atributo ou método) -->
<#macro beanElement elementName, javaClass=domain ><#t>
${"#"+"{"+gc.getComponentVarName("JSFBean", javaClass)+"."+elementName}<#nested>${"}"}<#t>
</#macro>


<#-- Coloca qualquer literal entre braces -->
<#macro brc literal="" ><#t>
${"#"+"{"}<#if literal!=""><#t>
	${literal}<#t>
<#else><#t>
	<#nested literal><#t>
</#if>${"}"}<#t>
</#macro>


<#-- printa argumentos de método de find-->
<#macro argumentosFind javaClass=domain, type=true>
	<@iterateFindArguments ",", javaClass ; p, sufix><#t>
		<#if type=true>${methodForProp(p).returns.getJavaClass().getName()} </#if>${p.name}${sufix}<#t>
	</@iterateFindArguments ><#t>
</#macro>


<#-- Propriedades pesquisaveis -->
<#macro propPesquisaveis javaClass=domain>
	<@iterateFindArguments "", javaClass ; p, sufix><#t>
	private ${methodForProp(p).returns.getJavaClass().getName()} ${p.name}${sufix};
	</@iterateFindArguments ><#t>
</#macro>


<#-- Propriedades pesquisaveis - Acessores -->
<#macro propPesquisaveisAccessors javaClass=domain>
	<@iterateFindArguments "", javaClass ; p, sufix><#t>
	/**
	 * Accessor Method
 	 * @return ${p.name}${sufix}
 	 */
	public ${methodForProp(p).returns.getJavaClass().getName()} ${p.accessor.name}${sufix}(){
		return this.${p.name}${sufix};
	}
	
	</@iterateFindArguments ><#t>
</#macro>


<#-- Propriedades pesquisaveis - Acessores -->
<#macro propPesquisaveisMutators javaClass=domain>
	<@iterateFindArguments "", javaClass ; p, sufix><#t>
	/**
	 * Mutator Method
 	 * @param ${p.name}${sufix}
 	 */
	public void ${p.mutator.name}${sufix}(${methodForProp(p).returns.getJavaClass().getName()} ${p.name}${sufix}){
		this.${p.name}${sufix} = ${p.name}${sufix};
	}
	
	</@iterateFindArguments ><#t>
</#macro>


<#-- 
	Printa argumentos de método de find.
	Se duplicate=true duplica atributos pesquisáveis com comparação do tipo RANGE. Ex: dtInclusao => dtInclusaoFrom e dtInclusaoTo
-->
<#macro iterateFindArguments pSeparator="" javaClass=domain duplicate=true>
	<#assign separator=""><#t>
	<@beanProperties ; p><#t>
		<#if hasTagSC(p, "JazzProp", "searchable", "true")><#t>
			<#if duplicate=true && hasTagSC(p, "JazzProp", "comparison", "RANGE")><#t>
				${separator}<#nested p "From"><#t>
				<#assign separator=pSeparator+" "><#t>
				${separator}<#nested p "To"><#t>
			<#else><#t>
				${separator}<#nested p ""><#t>
			</#if><#t>
			<#assign separator=pSeparator+" "><#t>
		</#if><#t>
	</@beanProperties><#t>
</#macro>


<#-- printa comentários para metodo find-->
<#macro comentariosFind javaClass=domain>
	/**
	 * Pesquisa entidades do tipo ${javaClass.name} 
	 * @author ${env.USER}
	<@iterateFindArguments "", javaClass ; p, sufix><#t>
	 * @param ${p.name}${sufix}
	</@iterateFindArguments ><#t>
	<#nested>
	 * @returns Coleção de ${javaClass.name}
	 */
</#macro>


<#-- determina o nome do metodo de find -->
<#macro metodoFind javaClass=domain>
	<@gc.isCriarFind><#t>
	find${javaClass.name}<#t>
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
		<#return annotation??><#--apenas testa se possui a annotation informada-->
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
<#function getAnnotationParamOld qdoxElement, tagName, parameter>
	<#assign a=getAnnotation(qdoxElement, tagName)>
	<#if a!="">
		<#return a.getProperty(parameter).getParameterValue()>
	<#else>
		<#return "">
	</#if>
</#function>

<#-- Recupera parametro da Annotacao solicitada -->
<#function getAnnotationParam qdoxElement, tagName, parameter>
	<#return modelLoader.getAnnotationValue(qdoxElement, tagName, parameter) />
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
		<#nested p><#-- Chama conteudo nested -->
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