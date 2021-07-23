<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="constants.ForwardConst" %>

<c:set var="actRep" value="${ForwardConst.ACT_REP.getValue()}" />
<c:set var="actReact" value="${ForwardConst.ACT_REACT.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commEdt" value="${ForwardConst.CMD_EDIT.getValue()}" />
<c:set var="commCrt" value="${ForwardConst.CMD_CREATE.getValue()}" />
<c:set var="commDst" value="${ForwardConst.CMD_DESTROY.getValue()}" />

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}" />
            </div>
        </c:if>
        <c:out value="${exception.getMessage()}" />
        <h2>日報 詳細ページ</h2>

        <table>
            <tbody>
                <tr>
                    <th>氏名</th>
                    <td><c:out value="${report.employee.name}" /></td>
                </tr>
                <tr>
                    <th>日付</th>
                    <fmt:parseDate value="${report.reportDate}" pattern="yyyy-MM-dd" var="reportDay" type="date" />
                    <td><fmt:formatDate value='${reportDay}' pattern='yyyy-MM-dd' /></td>
                </tr>
                <tr>
                    <th>内容</th>
                    <td><pre><c:out value="${report.content}" /></pre></td>
                </tr>
                <tr>
                    <th>登録日時</th>
                    <fmt:parseDate value="${report.createdAt}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="createDay" type="date" />
                    <td><fmt:formatDate value="${createDay}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                </tr>
                <tr>
                    <th>更新日時</th>
                    <fmt:parseDate value="${report.updatedAt}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="updateDay" type="date" />
                    <td><fmt:formatDate value="${updateDay}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                </tr>
                <tr>
                    <th>反応</th>
                    <td class="report_reaction">
                        <c:choose>
                            <c:when test="${is_good_react}">
                                <a href="#" onclick="(function(){var reactionType=0;doUnreaction(reactionType);})();">
                                    <i class="fas fa-thumbs-up"></i>
                                </a> ${react_good}&nbsp;&nbsp;
                            </c:when>
                            <c:otherwise>
                                <a href="#" onclick="(function(){var reactionType=0;doReaction(reactionType);})();">
                                    <i class="far fa-thumbs-up"></i>
                                </a> ${react_good}&nbsp;&nbsp;
                            </c:otherwise>
                        </c:choose>

                        <c:choose>
                            <c:when test="${is_praise_react}">
                                <a href="#" onclick="(function(){var reactionType=1;doUnreaction(reactionType);})();">
                                    <i class="fas fa-handshake"></i>
                                </a> ${react_praise}&nbsp;&nbsp;
                            </c:when>
                            <c:otherwise>
                                <a href="#" onclick="(function(){var reactionType=1;doReaction(reactionType);})();">
                                    <i class="far fa-handshake"></i>
                                </a> ${react_praise}&nbsp;&nbsp;
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </tbody>
        </table>

        <c:if test="${sessionScope.login_employee.id == report.employee.id}">
            <p>
                <a href="<c:url value='?action=${actRep}&command=${commEdt}&id=${report.id}' />">この日報を編集する</a>
            </p>
        </c:if>

        <p>
            <a href="<c:url value='?action=${actRep}&command=${commIdx}' />">一覧に戻る</a>
        </p>

        <form name="create" method="post" action="<c:url value='?action=${actReact}&command=${commCrt}' />">
            <input type="hidden" name="id" value="${report.id}">
            <input type="hidden" name="_token" value="${_token}">
            <input type="hidden" name="react_type">
        </form>
        <form name="destroy" method="post" action="<c:url value='?action=${actReact}&command=${commDst}' />">
            <input type="hidden" name="id" value="${report.id}">
            <input type="hidden" name="_token" value="${_token}">
            <input type="hidden" name="react_type">
        </form>
        <script>
            function doReaction(reactionType) {
                document.forms["create"].elements["react_type"].value = reactionType;
                document.forms["create"].submit();
            }
            function doUnreaction(reactionType) {
                document.forms["destroy"].elements["react_type"].value = reactionType;
                document.forms["destroy"].submit();
            }
        </script>
    </c:param>
</c:import>