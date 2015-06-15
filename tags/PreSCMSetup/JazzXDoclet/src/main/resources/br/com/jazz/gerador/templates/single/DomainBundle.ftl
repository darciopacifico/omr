pnl<@gc.componentName "DomainBundle"/>=${gc.getAnnotationParam(domain, "JazzClass", "name")?web_safe}

<@gc.beanProperties ; p>
${p.name}=${gc.getAnnotationParam(p, "JazzProp", "name")?web_safe}
</@gc.beanProperties>

