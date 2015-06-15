pnl<@gc.componentName "DomainBundle"/>=${gc.getAnnotationParam(domain, "JazzClass", "name")?web_safe}


<@gc.iterateProperties "", javaClass, false; p, prefix>
${p.name}${prefix}=${prefix} ${gc.getAnnotationParam(p, "JazzProp", "name")?web_safe}
</@gc.iterateProperties>

<@gc.iterateProperties "", javaClass, true; p, prefix>
	<#if gc.getAnnotationParam(p,"JazzProp","comparison")=="RANGE"><#t>
${p.name}${prefix}=${prefix} ${gc.getAnnotationParam(p, "JazzProp", "name")?web_safe}
	</#if>
</@gc.iterateProperties>

