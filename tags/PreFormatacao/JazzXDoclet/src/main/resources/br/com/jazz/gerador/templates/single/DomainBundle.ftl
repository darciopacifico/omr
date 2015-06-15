pnl<@gc.componentName "DomainBundle"/>=${gc.getAnnotationParam(domain, "br.com.jazz.codegen.annotation.JazzClass", "name")?web_safe}

<@gc.beanProperties ; p>
${p.name}=${gc.getAnnotationParam(p, "br.com.jazz.codegen.annotation.JazzProp", "name")?web_safe}
</@gc.beanProperties>

