package com.mysite.sbb;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Component;

//마크다운 컴포넌트

//빈으로 등록된 컴포넌트는 탬플릿에서 바로 사용할 수 있다.
@Component 
public class CommonUtil { 
	//CommonUtil 클래스는 스프링부트에 의해 관리되는 빈(bean, 자바객체)으로 등록된다.
	
	// markdown 메소드 생성
	public String markdown(String markdown) {
		
		Parser parser = Parser.builder().build();
		Node document = parser.parse(markdown);
		HtmlRenderer renderer = HtmlRenderer.builder().build();
		
		return renderer.render(document);
	}
	
}
