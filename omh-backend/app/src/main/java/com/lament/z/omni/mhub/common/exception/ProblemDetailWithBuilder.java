package com.lament.z.omni.mhub.common.exception;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ProblemDetail;

/**
 *
 * This Class is {@link ProblemDetail} with Builder.
 * A easy way to use ProblemDetail.
 * */
@SuppressWarnings("java:S2160")
public class ProblemDetailWithBuilder  extends ProblemDetail{

	private ProblemDetailWithBuilder problemDetail;

	public ProblemDetailWithBuilder(int rawStatusCode, ProblemDetailWithBuilder problemDetail) {
		super(rawStatusCode);
		this.problemDetail = problemDetail;
	}

	public ProblemDetailWithBuilder(int rawStatusCode) {
		super(rawStatusCode);
	}

	public ProblemDetailWithBuilder getProblemDetail() {
		return problemDetail;
	}

	public void setProblemDetail(ProblemDetailWithBuilder problemDetail) {
		this.problemDetail = problemDetail;
	}

	//
	public static class ProblemDetailBuilder {
		private static final URI BLANK_TYPE = URI.create("about:blank");
		// type 是 对错误进行详细解释的文档所对应的 url， 没有则默认为 "about:blank"
		private URI type = BLANK_TYPE;
		// 说人话版本的错误标题
		private String title;
		// httpStatus
		private int status;
		// 说人话版本的错误描述
		private String detail;
		// URI 类型的错误标识
		private URI instance;
		//其他
		private Map<String, Object> properties = new HashMap<>();
		private ProblemDetailWithBuilder problemDetail;

		public static ProblemDetailBuilder getBuilder(){
			return new ProblemDetailBuilder();
		}

		public ProblemDetailBuilder type(URI type){
			this.type = type;
			return this;
		}

		public ProblemDetailBuilder title(String title){
			this.title = title;
			return this;
		}

		public ProblemDetailBuilder status(int status){
			this.status = status;
			return this;
		}

		public ProblemDetailBuilder detail(String detail){
			this.detail = detail;
			return this;
		}

		public ProblemDetailBuilder instance(URI instance){
			this.instance = instance;
			return this;
		}

		public ProblemDetailBuilder problemDetail(ProblemDetailWithBuilder problemDetail){
			this.problemDetail = problemDetail;
			return this;
		}

		public ProblemDetailBuilder properties(Map<String, Object> properties){
			this.properties = properties;
			return this;
		}
		public ProblemDetailBuilder propertie(String key, Object value){
			this.properties.put(key, value);
			return this;
		}

		public ProblemDetailWithBuilder build(){
			ProblemDetailWithBuilder pd = new ProblemDetailWithBuilder(this.status);
			pd.setType(this.type);
			pd.setTitle(this.title);
			pd.setDetail(this.detail);
			pd.setInstance(this.instance);
			pd.setProblemDetail(this.problemDetail);
			this.properties.forEach(pd::setProperty);
			return pd;
		}

	}
}
