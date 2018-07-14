package po;

import java.util.ArrayList;
import java.util.List;

public class CodeParameterExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CodeParameterExample() {
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

        public Criteria andCodeParameterIdIsNull() {
            addCriterion("code_parameter_id is null");
            return (Criteria) this;
        }

        public Criteria andCodeParameterIdIsNotNull() {
            addCriterion("code_parameter_id is not null");
            return (Criteria) this;
        }

        public Criteria andCodeParameterIdEqualTo(Integer value) {
            addCriterion("code_parameter_id =", value, "codeParameterId");
            return (Criteria) this;
        }

        public Criteria andCodeParameterIdNotEqualTo(Integer value) {
            addCriterion("code_parameter_id <>", value, "codeParameterId");
            return (Criteria) this;
        }

        public Criteria andCodeParameterIdGreaterThan(Integer value) {
            addCriterion("code_parameter_id >", value, "codeParameterId");
            return (Criteria) this;
        }

        public Criteria andCodeParameterIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("code_parameter_id >=", value, "codeParameterId");
            return (Criteria) this;
        }

        public Criteria andCodeParameterIdLessThan(Integer value) {
            addCriterion("code_parameter_id <", value, "codeParameterId");
            return (Criteria) this;
        }

        public Criteria andCodeParameterIdLessThanOrEqualTo(Integer value) {
            addCriterion("code_parameter_id <=", value, "codeParameterId");
            return (Criteria) this;
        }

        public Criteria andCodeParameterIdIn(List<Integer> values) {
            addCriterion("code_parameter_id in", values, "codeParameterId");
            return (Criteria) this;
        }

        public Criteria andCodeParameterIdNotIn(List<Integer> values) {
            addCriterion("code_parameter_id not in", values, "codeParameterId");
            return (Criteria) this;
        }

        public Criteria andCodeParameterIdBetween(Integer value1, Integer value2) {
            addCriterion("code_parameter_id between", value1, value2, "codeParameterId");
            return (Criteria) this;
        }

        public Criteria andCodeParameterIdNotBetween(Integer value1, Integer value2) {
            addCriterion("code_parameter_id not between", value1, value2, "codeParameterId");
            return (Criteria) this;
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

        public Criteria andParameterTypeIsNull() {
            addCriterion("parameter_type is null");
            return (Criteria) this;
        }

        public Criteria andParameterTypeIsNotNull() {
            addCriterion("parameter_type is not null");
            return (Criteria) this;
        }

        public Criteria andParameterTypeEqualTo(String value) {
            addCriterion("parameter_type =", value, "parameterType");
            return (Criteria) this;
        }

        public Criteria andParameterTypeNotEqualTo(String value) {
            addCriterion("parameter_type <>", value, "parameterType");
            return (Criteria) this;
        }

        public Criteria andParameterTypeGreaterThan(String value) {
            addCriterion("parameter_type >", value, "parameterType");
            return (Criteria) this;
        }

        public Criteria andParameterTypeGreaterThanOrEqualTo(String value) {
            addCriterion("parameter_type >=", value, "parameterType");
            return (Criteria) this;
        }

        public Criteria andParameterTypeLessThan(String value) {
            addCriterion("parameter_type <", value, "parameterType");
            return (Criteria) this;
        }

        public Criteria andParameterTypeLessThanOrEqualTo(String value) {
            addCriterion("parameter_type <=", value, "parameterType");
            return (Criteria) this;
        }

        public Criteria andParameterTypeLike(String value) {
            addCriterion("parameter_type like", value, "parameterType");
            return (Criteria) this;
        }

        public Criteria andParameterTypeNotLike(String value) {
            addCriterion("parameter_type not like", value, "parameterType");
            return (Criteria) this;
        }

        public Criteria andParameterTypeIn(List<String> values) {
            addCriterion("parameter_type in", values, "parameterType");
            return (Criteria) this;
        }

        public Criteria andParameterTypeNotIn(List<String> values) {
            addCriterion("parameter_type not in", values, "parameterType");
            return (Criteria) this;
        }

        public Criteria andParameterTypeBetween(String value1, String value2) {
            addCriterion("parameter_type between", value1, value2, "parameterType");
            return (Criteria) this;
        }

        public Criteria andParameterTypeNotBetween(String value1, String value2) {
            addCriterion("parameter_type not between", value1, value2, "parameterType");
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