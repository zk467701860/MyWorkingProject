package po;

import java.util.ArrayList;
import java.util.List;

public class CodeImportExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CodeImportExample() {
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

        public Criteria andCodeImportIdIsNull() {
            addCriterion("code_import_id is null");
            return (Criteria) this;
        }

        public Criteria andCodeImportIdIsNotNull() {
            addCriterion("code_import_id is not null");
            return (Criteria) this;
        }

        public Criteria andCodeImportIdEqualTo(Integer value) {
            addCriterion("code_import_id =", value, "codeImportId");
            return (Criteria) this;
        }

        public Criteria andCodeImportIdNotEqualTo(Integer value) {
            addCriterion("code_import_id <>", value, "codeImportId");
            return (Criteria) this;
        }

        public Criteria andCodeImportIdGreaterThan(Integer value) {
            addCriterion("code_import_id >", value, "codeImportId");
            return (Criteria) this;
        }

        public Criteria andCodeImportIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("code_import_id >=", value, "codeImportId");
            return (Criteria) this;
        }

        public Criteria andCodeImportIdLessThan(Integer value) {
            addCriterion("code_import_id <", value, "codeImportId");
            return (Criteria) this;
        }

        public Criteria andCodeImportIdLessThanOrEqualTo(Integer value) {
            addCriterion("code_import_id <=", value, "codeImportId");
            return (Criteria) this;
        }

        public Criteria andCodeImportIdIn(List<Integer> values) {
            addCriterion("code_import_id in", values, "codeImportId");
            return (Criteria) this;
        }

        public Criteria andCodeImportIdNotIn(List<Integer> values) {
            addCriterion("code_import_id not in", values, "codeImportId");
            return (Criteria) this;
        }

        public Criteria andCodeImportIdBetween(Integer value1, Integer value2) {
            addCriterion("code_import_id between", value1, value2, "codeImportId");
            return (Criteria) this;
        }

        public Criteria andCodeImportIdNotBetween(Integer value1, Integer value2) {
            addCriterion("code_import_id not between", value1, value2, "codeImportId");
            return (Criteria) this;
        }

        public Criteria andImportPackageIsNull() {
            addCriterion("import_package is null");
            return (Criteria) this;
        }

        public Criteria andImportPackageIsNotNull() {
            addCriterion("import_package is not null");
            return (Criteria) this;
        }

        public Criteria andImportPackageEqualTo(String value) {
            addCriterion("import_package =", value, "importPackage");
            return (Criteria) this;
        }

        public Criteria andImportPackageNotEqualTo(String value) {
            addCriterion("import_package <>", value, "importPackage");
            return (Criteria) this;
        }

        public Criteria andImportPackageGreaterThan(String value) {
            addCriterion("import_package >", value, "importPackage");
            return (Criteria) this;
        }

        public Criteria andImportPackageGreaterThanOrEqualTo(String value) {
            addCriterion("import_package >=", value, "importPackage");
            return (Criteria) this;
        }

        public Criteria andImportPackageLessThan(String value) {
            addCriterion("import_package <", value, "importPackage");
            return (Criteria) this;
        }

        public Criteria andImportPackageLessThanOrEqualTo(String value) {
            addCriterion("import_package <=", value, "importPackage");
            return (Criteria) this;
        }

        public Criteria andImportPackageLike(String value) {
            addCriterion("import_package like", value, "importPackage");
            return (Criteria) this;
        }

        public Criteria andImportPackageNotLike(String value) {
            addCriterion("import_package not like", value, "importPackage");
            return (Criteria) this;
        }

        public Criteria andImportPackageIn(List<String> values) {
            addCriterion("import_package in", values, "importPackage");
            return (Criteria) this;
        }

        public Criteria andImportPackageNotIn(List<String> values) {
            addCriterion("import_package not in", values, "importPackage");
            return (Criteria) this;
        }

        public Criteria andImportPackageBetween(String value1, String value2) {
            addCriterion("import_package between", value1, value2, "importPackage");
            return (Criteria) this;
        }

        public Criteria andImportPackageNotBetween(String value1, String value2) {
            addCriterion("import_package not between", value1, value2, "importPackage");
            return (Criteria) this;
        }

        public Criteria andImportClassIsNull() {
            addCriterion("import_class is null");
            return (Criteria) this;
        }

        public Criteria andImportClassIsNotNull() {
            addCriterion("import_class is not null");
            return (Criteria) this;
        }

        public Criteria andImportClassEqualTo(String value) {
            addCriterion("import_class =", value, "importClass");
            return (Criteria) this;
        }

        public Criteria andImportClassNotEqualTo(String value) {
            addCriterion("import_class <>", value, "importClass");
            return (Criteria) this;
        }

        public Criteria andImportClassGreaterThan(String value) {
            addCriterion("import_class >", value, "importClass");
            return (Criteria) this;
        }

        public Criteria andImportClassGreaterThanOrEqualTo(String value) {
            addCriterion("import_class >=", value, "importClass");
            return (Criteria) this;
        }

        public Criteria andImportClassLessThan(String value) {
            addCriterion("import_class <", value, "importClass");
            return (Criteria) this;
        }

        public Criteria andImportClassLessThanOrEqualTo(String value) {
            addCriterion("import_class <=", value, "importClass");
            return (Criteria) this;
        }

        public Criteria andImportClassLike(String value) {
            addCriterion("import_class like", value, "importClass");
            return (Criteria) this;
        }

        public Criteria andImportClassNotLike(String value) {
            addCriterion("import_class not like", value, "importClass");
            return (Criteria) this;
        }

        public Criteria andImportClassIn(List<String> values) {
            addCriterion("import_class in", values, "importClass");
            return (Criteria) this;
        }

        public Criteria andImportClassNotIn(List<String> values) {
            addCriterion("import_class not in", values, "importClass");
            return (Criteria) this;
        }

        public Criteria andImportClassBetween(String value1, String value2) {
            addCriterion("import_class between", value1, value2, "importClass");
            return (Criteria) this;
        }

        public Criteria andImportClassNotBetween(String value1, String value2) {
            addCriterion("import_class not between", value1, value2, "importClass");
            return (Criteria) this;
        }

        public Criteria andImportTypeIsNull() {
            addCriterion("import_type is null");
            return (Criteria) this;
        }

        public Criteria andImportTypeIsNotNull() {
            addCriterion("import_type is not null");
            return (Criteria) this;
        }

        public Criteria andImportTypeEqualTo(String value) {
            addCriterion("import_type =", value, "importType");
            return (Criteria) this;
        }

        public Criteria andImportTypeNotEqualTo(String value) {
            addCriterion("import_type <>", value, "importType");
            return (Criteria) this;
        }

        public Criteria andImportTypeGreaterThan(String value) {
            addCriterion("import_type >", value, "importType");
            return (Criteria) this;
        }

        public Criteria andImportTypeGreaterThanOrEqualTo(String value) {
            addCriterion("import_type >=", value, "importType");
            return (Criteria) this;
        }

        public Criteria andImportTypeLessThan(String value) {
            addCriterion("import_type <", value, "importType");
            return (Criteria) this;
        }

        public Criteria andImportTypeLessThanOrEqualTo(String value) {
            addCriterion("import_type <=", value, "importType");
            return (Criteria) this;
        }

        public Criteria andImportTypeLike(String value) {
            addCriterion("import_type like", value, "importType");
            return (Criteria) this;
        }

        public Criteria andImportTypeNotLike(String value) {
            addCriterion("import_type not like", value, "importType");
            return (Criteria) this;
        }

        public Criteria andImportTypeIn(List<String> values) {
            addCriterion("import_type in", values, "importType");
            return (Criteria) this;
        }

        public Criteria andImportTypeNotIn(List<String> values) {
            addCriterion("import_type not in", values, "importType");
            return (Criteria) this;
        }

        public Criteria andImportTypeBetween(String value1, String value2) {
            addCriterion("import_type between", value1, value2, "importType");
            return (Criteria) this;
        }

        public Criteria andImportTypeNotBetween(String value1, String value2) {
            addCriterion("import_type not between", value1, value2, "importType");
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