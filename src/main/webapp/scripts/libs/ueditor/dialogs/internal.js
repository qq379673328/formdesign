(function () {
    var parent = window.parent;
    //dialog对象
    dialog = parent.$EDITORUI[window.frameElement.id.replace( /_iframe$/, '' )];
    //当前打开dialog的编辑器实例
    editor = dialog.editor;
    UE = parent.UE;
    domUtils = UE.dom.domUtils;
    utils = UE.utils;
    browser = UE.browser;
    ajax = UE.ajax;
    /*公共方法*/
    $G = function ( id ) {
        return document.getElementById( id );
    };
    //focus元素
    $focus = function ( node ) {
        setTimeout( function () {
            if ( browser.ie ) {
                var r = node.createTextRange();
                r.collapse( false );
                r.select();
            } else {
                node.focus();
            }
        }, 0 );
    };
    utils.loadFile(document,{
        href:editor.options.themePath + editor.options.theme + "/dialogbase.css?cache="+Math.random(),
        tag:"link",
        type:"text/css",
        rel:"stylesheet"
    });
    lang = editor.getLang(dialog.className.split( "-" )[2]);
    if(lang){
        domUtils.on(window,'load',function () {

            var langImgPath = editor.options.langPath + editor.options.lang + "/images/";
            //针对静态资源
            for ( var i in lang["static"] ) {
                var dom = $G( i );
                if(!dom) continue;
                var tagName = dom.tagName,
                    content = lang["static"][i];
                if(content.src){
                    //clone
                    content = utils.extend({},content,false);
                    content.src = langImgPath + content.src;
                }
                if(content.style){
                    content = utils.extend({},content,false);
                    content.style = content.style.replace(/url\s*\(/g,"url(" + langImgPath);
                }
                switch ( tagName.toLowerCase() ) {
                    case "var":
                        dom.parentNode.replaceChild( document.createTextNode( content ), dom );
                        break;
                    case "select":
                        var ops = dom.options;
                        for ( var j = 0, oj; oj = ops[j]; ) {
                            oj.innerHTML = content.options[j++];
                        }
                        for ( var p in content ) {
                            p != "options" && dom.setAttribute( p, content[p] );
                        }
                        break;
                    default :
                        domUtils.setAttributes( dom, content);
                }
            }
        } );
    }

})();
/*公共方法*/
function createElement(type, name) {
	var element = null;
	try {
		element = document.createElement('<'+type+' name="'+name+'">');
	} catch (e) {
	}
	if (element == null) {
		element = document.createElement(type);
		element.name = name;
	}
	return element;
}
function isIE(){
    if(window.attachEvent){   
        return true;
    }
    return false;
}

//moveRow在IE支持而在火狐里不支持！以下是扩展火狐下的moveRow
if (!isIE()) {
  function getTRNode(nowTR, sibling) {
      while (nowTR = nowTR[sibling]) if (nowTR.tagName == 'TR') break;
      return nowTR;
  }
  if (typeof Element != 'undefined') {
      Element.prototype.moveRow = function(sourceRowIndex, targetRowIndex) //执行扩展操作
      {
          if (!/^(table|tbody|tfoot|thead)$/i.test(this.tagName) || sourceRowIndex === targetRowIndex) return false;
          var pNode = this;
          if (this.tagName == 'TABLE') pNode = this.getElementsByTagName('tbody')[0]; //firefox会自动加上tbody标签，所以需要取tbody，直接table.insertBefore会error
          var sourceRow = pNode.rows[sourceRowIndex],
          targetRow = pNode.rows[targetRowIndex];
          if (sourceRow == null || targetRow == null) return false;
          var targetRowNextRow = sourceRowIndex > targetRowIndex ? false: getTRNode(targetRow, 'nextSibling');
          if (targetRowNextRow === false) pNode.insertBefore(sourceRow, targetRow); //后面行移动到前面，直接insertBefore即可
          else { //移动到当前行的后面位置，则需要判断要移动到的行的后面是否还有行，有则insertBefore，否则appendChild
              if (targetRowNextRow == null) pNode.appendChild(sourceRow);
              else pNode.insertBefore(sourceRow, targetRowNextRow);
          }
      };
  }
}
/*删除tr*/
function fnDeleteRow(obj)
{
  var oTable = document.getElementById("options_table");
  while(obj.tagName !='TR')
  {
      obj = obj.parentNode;
  }
  oTable.deleteRow(obj.rowIndex);
}
/*上移*/
function fnMoveUp(obj)
{
  var oTable = document.getElementById("options_table");
  while(obj.tagName !='TR')
  {
      obj = obj.parentNode;
  }
  var minRowIndex = 1;
  var curRowIndex = obj.rowIndex;
  if(curRowIndex-1>=minRowIndex)
  {
      oTable.moveRow(curRowIndex,curRowIndex-1); 
  }
  
}
/*下移*/
function fnMoveDown(obj)
{
  var oTable = document.getElementById("options_table");
  while(obj.tagName !='TR')
  {
      obj = obj.parentNode;
  }
  var maxRowIndex = oTable.rows.length;
  var curRowIndex = obj.rowIndex;
  if(curRowIndex+1<maxRowIndex)
  {
      oTable.moveRow(curRowIndex,curRowIndex+1); 
  }
}