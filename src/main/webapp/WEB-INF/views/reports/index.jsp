<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="actRep" value="${ForwardConst.ACT_REP.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commShow" value="${ForwardConst.CMD_SHOW.getValue()}" />
<c:set var="commNew" value="${ForwardConst.CMD_NEW.getValue()}" />

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <h2>日報　一覧</h2>
        <table id="report_list">
            <tbody>
                <tr>
                    <th class="report_name">氏名</th>
                    <th class="report_date">日付</th>
                    <th class="report_title">タイトル</th>
                    <th class="report_action">操作</th>
                    <th class="report_reaction">反応</th>
                </tr>
                <c:if test="${0 < reports.size()}">
                    <c:forEach var="i" begin="0" end="${reports.size()-1}" step="1">
                        <fmt:parseDate value="${reports.get(i).reportDate}" pattern="yyyy-MM-dd" var="reportDay" type="date" />

                        <tr class="row${(i+1) % 2}">
                            <td class="report_name"><c:out value="${reports.get(i).employee.name}" /></td>
                            <td class="report_date"><fmt:formatDate value='${reportDay}' pattern='yyyy-MM-dd' /></td>
                            <td class="report_title">${reports.get(i).title}</td>
                            <td class="report_action"><a href="<c:url value='?action=${actRep}&command=${commShow}&id=${reports.get(i).id}' />">詳細を見る</a></td>
                            <td class="report_reaction">
                                <i class="far fa-thumbs-up"></i> ${react_good_list.get(i)}&nbsp;&nbsp;
                                <i class="far fa-handshake"></i> ${react_praise_list.get(i)}
                            </td>
                        </tr>
                    </c:forEach>
                </c:if>
            </tbody>
        </table>

        <div id="pagination">
            （全 ${reports_count} 件）<br />
            <c:forEach var="i" begin="1" end="${((reports_count - 1) / maxRow) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='?action=${actRep}&command=${commIdx}&page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <p><a href="<c:url value='?action=${actRep}&command=${commNew}' />">新規日報の登録</a></p>

    </c:param>
</c:import>