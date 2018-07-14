package po;

import java.util.ArrayList;
import java.util.List;

public class CodeMethodExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CodeMethodExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andCodeMethodIdIsNull() {
            addCriterion("code_method_id is null");
            return (Criteria) this;
        }

        public Criteria andCodeMethodIdIsNotNull() {
            addCriterion("code_method_id is not null");
            return (Criteria) this;
        }

        public Criteria andCodeMethodIdEqualTo(Integer value) {
            addCriterion("code_method_id =", value, "codeMethodId");
            return (Criteria) this;
        }

        public Criteria andCodeMethodIdNotEqualTo(Integer value) {
            addCriterion("code_method_id <>", value, "codeMethodId");
            return (Criteria) this;
        }

        public Criteria andCodeMethodIdGreaterThan(Integer value) {
            addCriterion("code_method_id >", value, "codeMethodId");
            return (Criteria) this;
        }

        public Criteria andCodeMethodIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("code_method_id >=", value, "codeMethodId");
            return (Criteria) this;
        }

        public Criteria andCodeMethodIdLessThan(Integer value) {
            addCriterion("code_method_id <", value, "codeMethodId");
            return (Criteria) this;
        }

        public Criteria andCodeMethodIdLessThanOrEqualTo(Integer value) {
            addCriterion("code_method_id <=", value, "codeMethodId");
            return (Criteria) this;
        }

        public Criteria andCodeMethodIdIn(List<Integer> values) {
            addCriterion("code_method_id in", values, "codeMethodId");
            return (Criteria) this;
        }

        public Criteria andCodeMethodIdNotIn(List<Integer> values) {
            addCriterion("code_method_id not in", values, "codeMethodId");
            return (Criteria) this;
        }

        public Criteria andCodeMethodIdBetween(Integer value1, Integer value2) {
            addCriterion("code_method_id between", value1, value2, "codeMethodId");
            return (Criteria) this;
        }

        public Criteria andCodeMethodIdNotBetween(Integer value1, Integer value2) {
            addCriterion("code_method_id not between", value1, value2, "codeMethodId");
            return (Criteria) this;
        }

        public Criteria andCodeClassIdIsNull() {
            addCriterion("code_class_id is null");
            return (Criteria) this;
        }

        public Criteria andCodeClassIdIsNotNull() {
            addCriterion("code_class_id is not null");
            return (Criteria) this;
        }

        public Criteria andCodeClassIdEqualTo(Integer value) {
            addCriterion("code_class_id =", value, "codeClassId");
            return (Criteria) this;
        }

        public Criteria andCodeClassIdNotEqualTo(Integer value) {
            addCriterion("code_class_id <>", value, "codeClassId");
            return (Criteria) this;
        }

        public Criteria andCodeClassIdGreaterThan(Integer value) {
            addCriterion("code_class_id >", value, "codeClassId");
            return (Criteria) this;
        }

        public Criteria andCodeClassIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("code_class_id >=", value, "codeClassId");
            return (Criteria) this;
        }

        public Criteria andCodeClassIdLessThan(Integer value) {
            addCriterion("code_class_id <", value, "codeClassId");
            return (Criteria) this;
        }

        public Criteria andCodeClassIdLessThanOrEqualTo(Integer value) {
            addCriterion("code_class_id <=", value, "codeClassId");
            return (Criteria) this;
        }

        public Criteria andCodeClassIdIn(List<Integer> values) {
            addCriterion("code_class_id in", values, "codeClassId");
            return (Criteria) this;
        }

        public Criteria andCodeClassIdNotIn(List<Integer> values) {
            addCriterion("code_class_id not in", values, "codeClassId");
            return (Criteria) this;
        }

        public Criteria andCodeClassIdBetween(Integer value1, Integer value2) {
            addCriterion("code_class_id between", value1, value2, "codeClassId");
            return (Criteria) this;
        }

        public Criteria andCodeClassIdNotBetween(Integer value1, Integer value2) {
            addCriterion("code_class_id not between", value1, value2, "codeClassId");
            return (Criteria) this;
        }

        public Criteria andMethodSignuatureIsNull() {
            addCriterion("method_signuature is null");
            return (Criteria) this;
        }

        public Criteria andMethodSignuatureIsNotNull() {
            addCriterion("method_signuature is not null");
            return (Criteria) this;
        }

        public Criteria andMethodSignuatureEqualTo(String value) {
            addCriterion("method_signuature =", value, "methodSignuature");
            return (Criteria) this;
        }

        public Criteria andMethodSignuatureNotEqualTo(String value) {
            addCriterion("method_signuature <>", value, "methodSignuature");
            return (Criteria) this;
        }

        public Criteria andMethodSignuatureGreaterThan(String value) {
            addCriterion("method_signuature >", value, "methodSignuature");
            return (Criteria) this;
        }

        public Criteria andMethodSignuatureGreaterThanOrEqualTo(String value) {
            addCriterion("method_signuature >=", value, "methodSignuature");
            return (Criteria) this;
        }

        public Criteria andMethodSignuatureLessThan(String value) {
            addCriterion("method_signuature <", value, "methodSignuature");
            return (Criteria) this;
        }

        public Criteria andMethodSignuatureLessThanOrEqualTo(String value) {
            addCriterion("method_signuature <=", value, "methodSignuature");
            return (Criteria) this;
        }

        public Criteria andMethodSignuatureLike(String value) {
            addCriterion("method_signuature like", value, "methodSignuature");
            return (Criteria) this;
        }

        public Criteria andMethodSignuatureNotLike(String value) {
            addCriterion("method_signuature not like", value, "methodSignuature");
            return (Criteria) this;
        }

        public Criteria andMethodSignuatureIn(List<String> values) {
            addCriterion("method_signuature in", values, "methodSignuature");
            return (Criteria) this;
        }

        public Criteria andMethodSignuatureNotIn(List<String> values) {
            addCriterion("method_signuature not in", values, "methodSignuature");
            return (Criteria) this;
        }

        public Criteria andMethodSignuatureBetween(String value1, String value2) {
            addCriterion("method_signuature between", value1, value2, "methodSignuature");
            return (Criteria) this;
        }

        public Criteria andMethodSignuatureNotBetween(String value1, String value2) {
            addCriterion("method_signuature not between", value1, value2, "methodSignuature");
            return (Criteria) this;
        }

        public Criteria andMethodNameIsNull() {
            addCriterion("method_name is null");
            return (Criteria) this;
        }

        public Criteria andMethodNameIsNotNull() {
            addCriterion("method_name is not null");
            return (Criteria) this;
        }

        public Criteria andMethodNameEqualTo(String value) {
            addCriterion("method_name =", value, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameNotEqualTo(String value) {
            addCriterion("method_name <>", value, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameGreaterThan(String value) {
            addCriterion("method_name >", value, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameGreaterThanOrEqualTo(String value) {
            addCriterion("method_name >=", value, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameLessThan(String value) {
            addCriterion("method_name <", value, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameLessThanOrEqualTo(String value) {
            addCriterion("method_name <=", value, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameLike(String value) {
            addCriterion("method_name like", value, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameNotLike(String value) {
            addCriterion("method_name not like", value, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameIn(List<String> values) {
            addCriterion("method_name in", values, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameNotIn(List<String> values) {
            addCriterion("method_name not in", values, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameBetween(String value1, String value2) {
            addCriterion("method_name between", value1, value2, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameNotBetween(String value1, String value2) {
            addCriterion("method_name not between", value1, value2, "methodName");
            return (Criteria) this;
        }

        public Criteria andAccessIsNull() {
            addCriterion("access is null");
            return (Criteria) this;
        }

        public Criteria andAccessIsNotNull() {
            addCriterion("access is not null");
            return (Criteria) this;
        }

        public Criteria andAccessEqualTo(String value) {
            addCriterion("access =", value, "access");
            return (Criteria) this;
        }

        public Criteria andAccessNotEqualTo(String value) {
            addCriterion("access <>", value, "access");
            return (Criteria) this;
        }

        public Criteria andAccessGreaterThan(String value) {
            addCriterion("access >", value, "access");
            return (Criteria) this;
        }

        public Criteria andAccessGreaterThanOrEqualTo(String value) {
            addCriterion("access >=", value, "access");
            return (Criteria) this;
        }

        public Criteria andAccessLessThan(String value) {
            addCriterion("access <", value, "access");
            return (Criteria) this;
        }

        public Criteria andAccessLessThanOrEqualTo(String value) {
            addCriterion("access <=", value, "access");
            return (Criteria) this;
        }

        public Criteria andAccessLike(String value) {
            addCriterion("access like", value, "access");
            return (Criteria) this;
        }

        public Criteria andAccessNotLike(String value) {
            addCriterion("access not like", value, "access");
            return (Criteria) this;
        }

        public Criteria andAccessIn(List<String> values) {
            addCriterion("access in", values, "access");
            return (Criteria) this;
        }

        public Criteria andAccessNotIn(List<String> values) {
            addCriterion("access not in", values, "access");
            return (Criteria) this;
        }

        public Criteria andAccessBetween(String value1, String value2) {
            addCriterion("access between", value1, value2, "access");
            return (Criteria) this;
        }

        public Criteria andAccessNotBetween(String value1, String value2) {
            addCriterion("access not between", value1, value2, "access");
            return (Criteria) this;
        }

        public Criteria andIsStaticIsNull() {
            addCriterion("is_static is null");
            return (Criteria) this;
        }

        public Criteria andIsStaticIsNotNull() {
            addCriterion("is_static is not null");
            return (Criteria) this;
        }

        public Criteria andIsStaticEqualTo(Integer value) {
            addCriterion("is_static =", value, "isStatic");
            return (Criteria) this;
        }

        public Criteria andIsStaticNotEqualTo(Integer value) {
            addCriterion("is_static <>", value, "isStatic");
            return (Criteria) this;
        }

        public Criteria andIsStaticGreaterThan(Integer value) {
            addCriterion("is_static >", value, "isStatic");
            return (Criteria) this;
        }

        public Criteria andIsStaticGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_static >=", value, "isStatic");
            return (Criteria) this;
        }

        public Criteria andIsStaticLessThan(Integer value) {
            addCriterion("is_static <", value, "isStatic");
            return (Criteria) this;
        }

        public Criteria andIsStaticLessThanOrEqualTo(Integer value) {
            addCriterion("is_static <=", value, "isStatic");
            return (Criteria) this;
        }

        public Criteria andIsStaticIn(List<Integer> values) {
            addCriterion("is_static in", values, "isStatic");
            return (Criteria) this;
        }

        public Criteria andIsStaticNotIn(List<Integer> values) {
            addCriterion("is_static not in", values, "isStatic");
            return (Criteria) this;
        }

        public Criteria andIsStaticBetween(Integer value1, Integer value2) {
            addCriterion("is_static between", value1, value2, "isStatic");
            return (Criteria) this;
        }

        public Criteria andIsStaticNotBetween(Integer value1, Integer value2) {
            addCriterion("is_static not between", value1, value2, "isStatic");
            return (Criteria) this;
        }

        public Criteria andReturnTypeIsNull() {
            addCriterion("return_type is null");
            return (Criteria) this;
        }

        public Criteria andReturnTypeIsNotNull() {
            addCriterion("return_type is not null");
            return (Criteria) this;
        }

        public Criteria andReturnTypeEqualTo(String value) {
            addCriterion("return_type =", value, "returnType");
            return (Criteria) this;
        }

        public Criteria andReturnTypeNotEqualTo(String value) {
            addCriterion("return_type <>", value, "returnType");
            return (Criteria) this;
        }

        public Criteria andReturnTypeGreaterThan(String value) {
            addCriterion("return_type >", value, "returnType");
            return (Criteria) this;
        }

        public Criteria andReturnTypeGreaterThanOrEqualTo(String value) {
            addCriterion("return_type >=", value, "returnType");
            return (Criteria) this;
        }

        public Criteria andReturnTypeLessThan(String value) {
            addCriterion("return_type <", value, "returnType");
            return (Criteria) this;
        }

        public Criteria andReturnTypeLessThanOrEqualTo(String value) {
            addCriterion("return_type <=", value, "returnType");
            return (Criteria) this;
        }

        public Criteria andReturnTypeLike(String value) {
            addCriterion("return_type like", value, "returnType");
            return (Criteria) this;
        }

        public Criteria andReturnTypeNotLike(String value) {
            addCriterion("return_type not like", value, "returnType");
            return (Criteria) this;
        }

        public Criteria andReturnTypeIn(List<String> values) {
            addCriterion("return_type in", values, "returnType");
            return (Criteria) this;
        }

        public Criteria andReturnTypeNotIn(List<String> values) {
            addCriterion("return_type not in", values, "returnType");
            return (Criteria) this;
        }

        public Criteria andReturnTypeBetween(String value1, String value2) {
            addCriterion("return_type between", value1, value2, "returnType");
            return (Criteria) this;
        }

        public Criteria andReturnTypeNotBetween(String value1, String value2) {
            addCriterion("return_type not between", value1, value2, "returnType");
            return (Criteria) this;
        }

        public Criteria andTypeValueIsNull() {
            addCriterion("type_value is null");
            return (Criteria) this;
        }

        public Criteria andTypeValueIsNotNull() {
            addCriterion("type_value is not null");
            return (Criteria) this;
        }

        public Criteria andTypeValueEqualTo(String value) {
            addCriterion("type_value =", value, "typeValue");
            return (Criteria) this;
        }

        public Criteria andTypeValueNotEqualTo(String value) {
            addCriterion("type_value <>", value, "typeValue");
            return (Criteria) this;
        }

        public Criteria andTypeValueGreaterThan(String value) {
            addCriterion("type_value >", value, "typeValue");
            return (Criteria) this;
        }

        public Criteria andTypeValueGreaterThanOrEqualTo(String value) {
            addCriterion("type_value >=", value, "typeValue");
            return (Criteria) this;
        }

        public Criteria andTypeValueLessThan(String value) {
            addCriterion("type_value <", value, "typeValue");
            return (Criteria) this;
        }

        public Criteria andTypeValueLessThanOrEqualTo(String value) {
            addCriterion("type_value <=", value, "typeValue");
            return (Criteria) this;
        }

        public Criteria andTypeValueLike(String value) {
            addCriterion("type_value like", value, "typeValue");
            return (Criteria) this;
        }

        public Criteria andTypeValueNotLike(String value) {
            addCriterion("type_value not like", value, "typeValue");
            return (Criteria) this;
        }

        public Criteria andTypeValueIn(List<String> values) {
            addCriterion("type_value in", values, "typeValue");
            return (Criteria) this;
        }

        public Criteria andTypeValueNotIn(List<String> values) {
            addCriterion("type_value not in", values, "typeValue");
            return (Criteria) this;
        }

        public Criteria andTypeValueBetween(String value1, String value2) {
            addCriterion("type_value between", value1, value2, "typeValue");
            return (Criteria) this;
        }

        public Criteria andTypeValueNotBetween(String value1, String value2) {
            addCriterion("type_value not between", value1, value2, "typeValue");
            return (Criteria) this;
        }

        public Criteria andInnerClassIsNull() {
            addCriterion("inner_class is null");
            return (Criteria) this;
        }

        public Criteria andInnerClassIsNotNull() {
            addCriterion("inner_class is not null");
            return (Criteria) this;
        }

        public Criteria andInnerClassEqualTo(Integer value) {
            addCriterion("inner_class =", value, "innerClass");
            return (Criteria) this;
        }

        public Criteria andInnerClassNotEqualTo(Integer value) {
            addCriterion("inner_class <>", value, "innerClass");
            return (Criteria) this;
        }

        public Criteria andInnerClassGreaterThan(Integer value) {
            addCriterion("inner_class >", value, "innerClass");
            return (Criteria) this;
        }

        public Criteria andInnerClassGreaterThanOrEqualTo(Integer value) {
            addCriterion("inner_class >=", value, "innerClass");
            return (Criteria) this;
        }

        public Criteria andInnerClassLessThan(Integer value) {
            addCriterion("inner_class <", value, "innerClass");
            return (Criteria) this;
        }

        public Criteria andInnerClassLessThanOrEqualTo(Integer value) {
            addCriterion("inner_class <=", value, "innerClass");
            return (Criteria) this;
        }

        public Criteria andInnerClassIn(List<Integer> values) {
            addCriterion("inner_class in", values, "innerClass");
            return (Criteria) this;
        }

        public Criteria andInnerClassNotIn(List<Integer> values) {
            addCriterion("inner_class not in", values, "innerClass");
            return (Criteria) this;
        }

        public Criteria andInnerClassBetween(Integer value1, Integer value2) {
            addCriterion("inner_class between", value1, value2, "innerClass");
            return (Criteria) this;
        }

        public Criteria andInnerClassNotBetween(Integer value1, Integer value2) {
            addCriterion("inner_class not between", value1, value2, "innerClass");
            return (Criteria) this;
        }

        public Criteria andApiIsNull() {
            addCriterion("api is null");
            return (Criteria) this;
        }

        public Criteria andApiIsNotNull() {
            addCriterion("api is not null");
            return (Criteria) this;
        }

        public Criteria andApiEqualTo(Integer value) {
            addCriterion("api =", value, "api");
            return (Criteria) this;
        }

        public Criteria andApiNotEqualTo(Integer value) {
            addCriterion("api <>", value, "api");
            return (Criteria) this;
        }

        public Criteria andApiGreaterThan(Integer value) {
            addCriterion("api >", value, "api");
            return (Criteria) this;
        }

        public Criteria andApiGreaterThanOrEqualTo(Integer value) {
            addCriterion("api >=", value, "api");
            return (Criteria) this;
        }

        public Criteria andApiLessThan(Integer value) {
            addCriterion("api <", value, "api");
            return (Criteria) this;
        }

        public Criteria andApiLessThanOrEqualTo(Integer value) {
            addCriterion("api <=", value, "api");
            return (Criteria) this;
        }

        public Criteria andApiIn(List<Integer> values) {
            addCriterion("api in", values, "api");
            return (Criteria) this;
        }

        public Criteria andApiNotIn(List<Integer> values) {
            addCriterion("api not in", values, "api");
            return (Criteria) this;
        }

        public Criteria andApiBetween(Integer value1, Integer value2) {
            addCriterion("api between", value1, value2, "api");
            return (Criteria) this;
        }

        public Criteria andApiNotBetween(Integer value1, Integer value2) {
            addCriterion("api not between", value1, value2, "api");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}