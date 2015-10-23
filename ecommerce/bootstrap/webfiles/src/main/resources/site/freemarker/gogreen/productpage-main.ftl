<#include "../include/imports.ftl">
<div class="col-md-9 col-sm-9">
<#if result??>
  <#if !result?starts_with("Error")>
  <div class="alert alert-success">
  <#else>
  <div class="alert alert-danger">
  </#if>
${result}
</div>
</#if>
<#if document??>
  <@hst.link var="link" hippobean=document/>
  <div class="blog-post">
    <div class="blog-post-type">
      <i class="icon-shop"> </i>
    </div>
    <div class="blog-span">
      <h2>${document.title?html}</h2>
      <@hst.cmseditlink hippobean=document/>
      <#if document.images??>
        <div class="blog-post-featured-img img-overlay">
          <div class="cycle-slideshow frame1" data-cycle-slides="> .slider-img" data-cycle-swipe="true"
               data-cycle-prev=".cycle-prev" data-cycle-next=".cycle-next" data-cycle-overlay-fx-out="slideUp"
               data-cycle-overlay-fx-in="slideDown" data-cycle-timeout="0">
            <div class="fa fa-chevron-right cycle-next"></div>
            <div class="fa fa-chevron-left cycle-prev"></div>
            <div class="cycle-pager"></div>
            <#list document.images as item>
              <@hst.link var="img" hippobean=item/>
              <div class="slider-img">
                <img src="${img}"
                     alt="${document.title?html}">

                <div class="item-img-overlay">
                  <a class="portfolio-zoom icon-zoom"
                     href="${img}"
                     data-rel="prettyPhoto[portfolio]" title="${document.title?html}"></a>
                </div>
              </div>
            </#list>
          </div>
        </div>
      </#if>
      <div class="blog-post-body">
        <p>${document.introduction?html}</p>
        <@hst.html hippohtml=document.description/>
      </div>
      <div class="blog-post-details">
        <div class="blog-post-details-item blog-post-details-item-left icon-banknote">
          <#if commerceprice??>
            <span class="price"><@fmt.formatNumber value="${commerceprice.value.number}" type="currency" currencyCode="${commerceprice.value.currency}" /></span>
          </#if>
        </div>
      <#--
      <div class="blog-post-details-item blog-post-details-item-left rating-info">
        <span id="document-rating" data-score="3.5"></span>
      </div>
      -->
        <form method="post" action="<@hst.actionURL />">
          <input name="sku" type="hidden" value="${document.productID}"/>
          <button class="btn btn-lg pull-right" type="submit">
            ADD TO CART
          </button>
        </form>
      </div>
    </div>
  </div>
</#if>
</div>

<@hst.headContribution category="htmlHead">
  <link rel="stylesheet" href="<@hst.webfile path="/css/prettyPhoto.css"/>"/>
</@hst.headContribution>
<@hst.headContribution category="htmlHead">
  <link rel="stylesheet" href="<@hst.webfile path="/css/jquery.raty.css"/>"/>
</@hst.headContribution>
<@hst.headContribution category="htmlBodyEnd">
  <script type="text/javascript" src="<@hst.webfile path="/js/jquery-2.1.0.min.js"/>"></script>
</@hst.headContribution>
<@hst.headContribution category="htmlBodyEnd">
  <script type="text/javascript" src="<@hst.webfile path="/js/jquery.raty.js"/>"></script>
</@hst.headContribution>
<@hst.headContribution category="htmlBodyEnd">
  <script type="text/javascript" src="<@hst.webfile path="/js/jquery.cycle.js"/>"></script>
</@hst.headContribution>
<@hst.headContribution category="htmlBodyEnd">
  <script type="text/javascript" src="<@hst.webfile path="/js/jquery.prettyPhoto.js"/>"></script>
</@hst.headContribution>
<@hst.headContribution category="htmlBodyEnd">
  <script type="text/javascript">
    jQuery(document).ready(function ($) {
      /* Portfolio PrettyPhoto */
      $("a[data-rel^='prettyPhoto']").prettyPhoto({
        animation_speed: 'fast', /* fast/slow/normal */
        slideshow: 5000, /* false OR interval time in ms */
        autoplay_slideshow: false, /* true/false */
        opacity: 0.80  /* Value between 0 and 1 */
      });
    });

    $(
            '#comments .comment-rating > div, #document-rating')
            .raty(
            {
              score: function () {
                return $(this).attr('data-score');
              },
              readOnly: true,
              half: true,
              starType: 'i'
            });
    $('#rating-field')
            .raty(
            {
              targetText: 0,
              target: '#rating',
              targetType: 'score',
              targetKeep: true,
              starType: 'i'
            });
  </script>
</@hst.headContribution>