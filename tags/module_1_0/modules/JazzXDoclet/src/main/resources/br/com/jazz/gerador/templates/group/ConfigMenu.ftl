<?xml version="1.0" encoding="ISO-8859-1" ?>
<html>
	<head>
		<title>Menu Desenvolvedor</title>
	</head>
	<body>
	
		<b><u>Foram gerados módulos de sistema para as entidades a seguir:</u></b>
		<ul>
		<#list domains as d ><#t>
			<#if gc.isParentConcrete(d)>
				<li><a href="${writerFactory.getDirRelativePath(rulesFactory.getTemplateRules('RichFacesJSP'),d)}/<@gc.componentName 'RichFacesJSP', d/>.jsf">${d.name}</a></li>
			</#if>
		</#list>
				<li><a href="cxf">WebServices Disponibilizados</a></li>
		</ul>

	</body>
</html>
