package com.urise.webapp.serialization;

import com.urise.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements Serializator {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            Map<ContactType, Contact> contacts = r.getContacts();
            writeCollection(dos, contacts.entrySet(), entry -> {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue().getContact());
            });

            Map<SectionType, Section<?>> sections = r.getSections();
            writeCollection(dos, sections.entrySet(), entry -> {
                SectionType type = entry.getKey();
                Section<?> section = entry.getValue();
                dos.writeUTF(type.name());
                switch (type) {
                    case PERSONAL:
                    case OBJECTIVE:
                        dos.writeUTF((String) section.getContent());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                         writeCollection(dos, (List<String>) section.getContent(), dos::writeUTF);
                         break;
                    case EDUCATION:
                    case EXPERIENCE:
                            writeCollection(dos, (List<Organisation>) section.getContent(), deepEntry -> {
                                dos.writeUTF(deepEntry.getOrganisationName());
                                dos.writeUTF(deepEntry.getOrganisationUrl());
                                writeCollection(dos, deepEntry.getPositions(), deeperEntry -> {
                                    dos.writeUTF(deeperEntry.getPosition());
                                    dos.writeUTF(deeperEntry.getDescription());
                                    dos.writeUTF(deeperEntry.getBeginDate().toString());
                                    dos.writeUTF(deeperEntry.getEndDate().toString());
                                });
                            });
                            break;
                    default:
                        throw new IllegalStateException();
                }
            });
        }
    }

    private interface Writer<T> {
        void write(T element) throws IOException;
    }

    private <T> void writeCollection(DataOutputStream dos, Collection<T> collection, Writer<T> writer) throws IOException {
        dos.writeInt(collection.size());
        for (T entry : collection) {
            writer.write(entry);
        }
    }


    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            readItems(dis, () -> resume.setContact(ContactType.valueOf(dis.readUTF()), new Contact(dis.readUTF())));
            readItems(dis, () -> {
                SectionType type = SectionType.valueOf(dis.readUTF());
                resume.setSection(type, readSection(dis, type));
            });


            return resume;
        }
    }

    private interface Reader<T> {
        T read() throws IOException;
    }

    private interface Processor {
        void process() throws IOException;
    }

    private void readItems(DataInputStream dis, Processor processor) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            processor.process();
        }
    }

    private Section<?> readSection(DataInputStream dis, SectionType type) throws IOException {
        switch (type) {
            case PERSONAL:
            case OBJECTIVE:
                return new TextSection(dis.readUTF());
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                return new ListSection(readList(dis, dis::readUTF));
            case EDUCATION:
            case EXPERIENCE:
                return new OrganisationsListSection(readList(dis, () ->
                        new Organisation(dis.readUTF(), dis.readUTF(), readList(dis, () ->
                                new Organisation.Position(dis.readUTF(), dis.readUTF(), LocalDate.parse(dis.readUTF()), LocalDate.parse(dis.readUTF()))))));
            default:
                throw new IllegalStateException();
        }
    }

    private <T> List<T> readList(DataInputStream dis, Reader<T> reader) throws IOException {
        int size = dis.readInt();
        List<T> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(reader.read());
        }
        return list;
    }
}
