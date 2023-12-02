package ru.croc.informationalportal.model;

public interface ICandidateDao {
    Candidate createCandidate(Candidate candidate);
    Candidate findCandidate(long phoneNumber);
    void deleteCandidate(long phoneNumber);
}
