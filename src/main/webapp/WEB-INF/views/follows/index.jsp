<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="constants.AttributeConst" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="actFlw" value="${ForwardConst.ACT_FLW.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commCrt" value="${ForwardConst.CMD_CREATE.getValue()}" />
<c:set var="commDst" value="${ForwardConst.CMD_DESTROY.getValue()}" />

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success"><c:out value="${flush}" /></div>
        </c:if>
        <h2>従業員　一覧</h2>
        <table id="employee_list">
            <tbody>
                <tr>
                    <th>社員番号</th>
                    <th>氏名</th>
                    <th>操作</th>
                </tr>
                <c:forEach var="i" begin="0" end="${employees.size()-1}">
                    <tr class="row${(i+1) % 2}">
                        <td><c:out value="${employees.get(i).code}" /></td>
                        <td><c:out value="${employees.get(i).name}" /></td>
                        <td>
                            <c:choose>
                                <c:when test="${employees.get(i).deleteFlag == AttributeConst.DEL_FLAG_TRUE.getIntegerValue()}">
                                    (削除済み)
                                </c:when>
                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${is_follow_list.get(i) == true}">
                                            <a href="#" onclick="(function(){
                                                                    var id = ${employees.get(i).id};
                                                                    doUnfollow(id);
                                                                 })();">フォロー解除</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="#" onclick="(function(){
                                                                    var id = ${employees.get(i).id};
                                                                    doFollow(id);
                                                                 })();">フォロー</a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <form name="create" method="POST" action="<c:url value='?action=${actFlw}&command=${commCrt}' />">
            <input type="hidden" name="id">
            <input type="hidden" name="_token" value="${_token}">
        </form>
        <form name="destroy" method="POST" action="<c:url value='?action=${actFlw}&command=${commDst}' />">
            <input type="hidden" name="id">
            <input type="hidden" name="_token" value="${_token}">
        </form>
        <script>
            function doFollow(id) {
                document.forms["create"].elements["id"].value = id;
                document.forms["create"].submit();
            }
            function doUnfollow(id) {
                document.forms["destroy"].elements["id"].value = id;
                document.forms["destroy"].submit();
            }
        </script>
    </c:param>
</c:import>