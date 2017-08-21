package ex;


import com.sun.istack.internal.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * Need to use when we want to create immutable object
 * Can coexist with setters but than it is not useful as object can be changed afterwards
 * All the validation at object states can be handled at builder pattern
 */
@Getter
@Builder
@ToString
public class BuilderConcept {

    @NonNull
    private String name;
    private Integer age;
    private String uId;

    public static void main(String [] args){
        BuilderConcept builderConcept = BuilderConcept.builder().name("Ajay").build();
        System.out.println(builderConcept);
    }
}
