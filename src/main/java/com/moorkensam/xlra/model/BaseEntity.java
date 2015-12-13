package com.moorkensam.xlra.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@MappedSuperclass
public abstract class BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected long id;

  @Version
  protected long version;

  @Temporal(TemporalType.TIMESTAMP)
  private Date createdDateTime;

  @Temporal(TemporalType.TIMESTAMP)
  private Date lastUpdatedDateTime;

  @Temporal(TemporalType.TIMESTAMP)
  private Date deletedDateTime;

  private boolean deleted;

  @PrePersist
  public void onCreate() {
    setCreatedDateTime(new Date());
  }

  @PreUpdate
  public void onUpdate() {
    setLastUpdatedDateTime(new Date());
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Date getCreatedDateTime() {
    return createdDateTime;
  }

  public void setCreatedDateTime(Date createdDateTime) {
    this.createdDateTime = createdDateTime;
  }

  public Date getLastUpdatedDateTime() {
    return lastUpdatedDateTime;
  }

  public void setLastUpdatedDateTime(Date lastUpdatedDateTime) {
    this.lastUpdatedDateTime = lastUpdatedDateTime;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  public Date getDeletedDateTime() {
    return deletedDateTime;
  }

  public void setDeletedDateTime(Date deletedDateTime) {
    this.deletedDateTime = deletedDateTime;
  }

  public long getVersion() {
    return version;
  }

  public void setVersion(long version) {
    this.version = version;
  }

}
