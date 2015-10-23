<#include "../include/imports.ftl">
<#if menu??>
  <#if menu.siteMenuItems??>
    <nav>
      <ul class="navigation" id="main-navigation">
        <#list menu.siteMenuItems as item>
          <#if  item.selected || item.expanded>
            <li><a href="<@hst.link link=item.hstLink/>" class="activelink"><span class="label-nav">${item.name?html}</span> </a></li>
          <#else>
            <li><a href="<@hst.link link=item.hstLink/>"><span class="label-nav">${item.name?html}</span></a></li>
          </#if>
        </#list>
      </ul>
    </nav>
  </#if>
  <@hst.cmseditmenu menu=menu/>
<#else>
  <#if editMode>
    <h5>[Menu Component]</h5>
    <sub>Click to edit Menu</sub>
  </#if>
</#if>