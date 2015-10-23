<#include "../include/imports.ftl">
<div class="col-md-3 col-sm-3">
  <#if facets??>
    <div class="hst-container">
      <div class="hst-container-item">
        <#list facets.folders as facet>        
          <div class="sidebar-block">
            <h3 class="h3-sidebar-title sidebar-title">${facet.name?html}</h3>
            <div class="sidebar-content tags">
            <#list facet.folders as value>
              <#if value.count &gt; 0>
                <#if value.leaf>
                  <@hst.facetnavigationlink var="link" remove=value current=facets />
                  <a href="${link}" class="remove">${value.name?html}<i class="fa fa-times"> </i></a>
                <#else>
                  <@hst.link var="link" hippobean=value />
                  <a href="${link}">${value.name?html}&nbsp;(${value.count})</a>
                </#if>
              </#if>
            </#list>
            </div>
          </div>
        </#list>
      </div>
    </div>
  </#if>
</div>