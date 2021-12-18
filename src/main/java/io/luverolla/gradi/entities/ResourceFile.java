package io.luverolla.gradi.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.luverolla.gradi.structures.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "gradi_resource_files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResourceFile extends BaseEntity
{
	@Column(nullable = false, unique = true)
	private String uri;
	
	@Column(nullable = false, unique = true)
	private String path;
	
	@Column(nullable = false)
	private String name;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;
	
	public String getURI()
	{
		return "/files/" + uri;
	}
}
