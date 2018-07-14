package extraction.schema.entity;

public class ConstraintEntity extends Entity {
    private String constraintDescription;
    private String precondition;
    private String conditionType;
    private String type;
    
    public ConstraintEntity() {
    }
    
    public ConstraintEntity(String constraintDescription) {
        this.setConstraintDescription(constraintDescription);
    }
    
    public String getPrecondition() {
		return precondition;
	}

	public void setPrecondition(String precondition) {
		this.precondition = precondition;
		this.addProperty("precondition", this.precondition);
	}

	public String getConditionType() {
		return conditionType;
	}

	public void setConditionType(String conditionType) {
		this.conditionType = conditionType;
		this.addProperty("conditionType", this.conditionType);
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		this.addProperty("type", this.type);
    }

    public String getConstraintDescription() {
        return constraintDescription;
    }

    public void setConstraintDescription(String constraintDescription) {
        this.constraintDescription = constraintDescription;
        this.addProperty("description", this.constraintDescription);
    }
}
