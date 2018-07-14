

public class GitRepository {
	private int repositoryId;
	private String repositoryName;
	private String repositoryPath;

	public GitRepository() {
	}

	public GitRepository(int repositoryId, String repositoryName, String repositoryPath) {
		this.repositoryId = repositoryId;
		this.repositoryName = repositoryName;
		this.repositoryPath = repositoryPath;
	}

	public int getRepositoryId() {
		return repositoryId;
	}

	public void setRepositoryId(int repositoryId) {
		this.repositoryId = repositoryId;
	}

	public String getRepositoryName() {
		return repositoryName;
	}

	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}

	public String getRepositoryPath() {
		return repositoryPath;
	}

	public void setRepositoryPath(String repositoryPath) {
		this.repositoryPath = repositoryPath;
	}
}
