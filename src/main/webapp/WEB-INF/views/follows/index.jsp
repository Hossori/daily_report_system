<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="constants.AttributeConst" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="actFlw" value="${ForwardConst.ACT_FLW.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commCrt" value="${ForwardConst.CMD_CREATE.getValue()}" />
<c:set var="commDst" value="${ForwardConst.CMD_DESTROY.getValue()}" />

<c:import url="../layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div class="flush_success"><c:out value="${flush}" /></div>
        </c:if>
        <c:if test="${errors != null}">
            <div class="flush_error">
                <c:forEach var="error" items="${errors}">
                    <c:out value="${error}" />
                </c:forEach>
            </div>
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
                                            <a href="<c:url value='?action=${actFlw}&command=${commDst}&id=${employees.get(i).id}' />">フォロー解除</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="<c:url value='?action=${actFlw}&command=${commCrt}&id=${employees.get(i).id}' />">フォロー</a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <input type="hidden" name="${AttributeConst.TOKEN}" value="${_token}">
    </c:param>
</c:import>