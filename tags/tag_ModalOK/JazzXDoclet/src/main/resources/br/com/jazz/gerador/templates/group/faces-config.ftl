<?xml version="1.0"?>
<!DOCTYPE faces-config PUBLIC "-//Sun Microsystems, Inc.//DTD JavaServer Faces Config 1.1//EN"
                              "http://java.sun.com/dtd/web-facesconfig_1_1.dtd">
<faces-config>

	<application>
		<view-handler>com.sun.facelets.FaceletViewHandler</view-handler>
		<el-resolver>org.springframework.web.jsf.el.SpringBeanFacesELResolver</el-resolver>
	</application>

<#list domains as d >
	
	<!--${d.name}-->
	<managed-bean>
		<managed-bean-name><@gc.componentVarName "JSFBean",d/></managed-bean-name>
		<managed-bean-class>${d.package.name}.<@gc.componentName "JSFBean",d/></managed-bean-class>
		<managed-bean-scope>session</managed-bean-scope>
	</managed-bean>
</#list>

	<navigation-rule>
		<from-view-id>/pages/index.jsp</from-view-id>
		<navigation-case>
			<from-outcome>Pag2</from-outcome>
			<to-view-id>/pages/Pag2.jsp</to-view-id>
		</navigation-case>
	</navigation-rule>
	<navigation-rule>
		<from-view-id>/pages/Pag2.jsp</from-view-id>
		<navigation-case>
			<from-outcome>Pag3</from-outcome>
			<to-view-id>/pages/Pag3.jsp</to-view-id>
		</navigation-case>
	</navigation-rule>
</faces-config>
