<#include "../include/imports.ftl">
<#if document.relatednews?has_content>
  <div class="col-md-3 col-sm-3">
    <div class="hst-container">
      <div class="hst-container-item">
        <div class="sidebar-block">
          <h3 class="h3-sidebar-title sidebar-title">Related News</h3>
          <div class="sidebar-content">
            <ul>
              <#list document.relatednews as item>
                <@hst.link var="link" hippobean=item />
                <li>
                  <a href="${link}">${item.title?html}</a>
                </li>
              </#list>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</#if>