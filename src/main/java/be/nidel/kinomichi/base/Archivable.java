package be.nidel.kinomichi.base;

public interface Archivable {
    boolean isArchived();
    void setArchived();
    void recoverArchive();
}
