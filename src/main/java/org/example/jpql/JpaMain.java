package org.example.jpql;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀A");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);


            /*
            TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class); //Entity 형식으로 받을 시
            List<Member> resultList = query.getResultList();
            for (Member member1 : resultList) {
                System.out.println("member1 = " + member1);
            }

            TypedQuery<String> query1 = em.createQuery("select m.username from Member m", String.class); //값 같은 타입 으로 받을 시
            Query query2 = em.createQuery("select m.username, m.age from Member m"); //타입 섞여 있는 경우

            //결과가 정확히 하나가 나와야함
            //결과가 없으면 NoResultException 둘 이상이면 NonUniqueResultException 나옴
            Member result = query.getSingleResult(); // 단건으로 뽑을시
            System.out.println("singleResult = " + result);

            TypedQuery<Member> query = em.createQuery("select m from Member m where username=:username", Member.class); //Entity 형식으로 받을 시
            query.setParameter("username", "member1");
            Member singleResult = query.getSingleResult();

            System.out.println("singleResult = " + singleResult.getUsername());

            //이런식으로 합쳐서 사용함 순서 형태로도 사용가능 권장x 순서가 밀리거나 도중 추가할 시 장애로 이어질 수 있음.
            Member result = em.createQuery("select m from Member m where username=?1", Member.class) //Entity 형식으로 받을 시
                    .setParameter(1, "member1")
                    .getSingleResult();

            //이런식으로 합쳐서 사용함 문자형태로 권장.
            Member result = em.createQuery("select m from Member m where username=:username", Member.class) //Entity 형식으로 받을 시
                    .setParameter("username", "member1")
                    .getSingleResult();

            //이런식으로 합쳐서 사용함 문자형태로 권장.
            Member result = em.createQuery("select m from Member m where username=:username", Member.class) //Entity 형식으로 받을 시
                    .setParameter("username", "member1")
                    .getSingleResult();

            System.out.println("singleResult = " + result);

            //수정 구현
            List<Member> result = em.createQuery("select m from Member m", Member.class).getResultList();

            Member findMember = result.get(0);
            findMember.setAge(20); //조회후 값 변경시 영속성 컨텍스트에서 관리되어서 바로 수정됨

            //자동으로 join 쿼리가 출력 됨 권장 x
            List<Team> result = em.createQuery("select m.team from Member m", Team.class).getResultList();
            //join을 적는게 더 명확하다 위에 방법으로 진행 시 예측이 잘 안되고 가독성이 떨어짐
            List<Team> result = em.createQuery("select t from Member m join m.team t", Team.class).getResultList();

            //어디 소속인지 적어줘야함 Aderss로 조회는 x 임베디드 프로젝션
            em.createQuery("select o.address from Order o", Address.class).getResultList();

            List resultList = em.createQuery("select m.username, m.age  from Member m").getResultList();
            Object o = resultList.get(0);
            Object[] result = (Object[]) o;
            System.out.println("result[0] = " + result[0]);
            System.out.println("result[1] = " + result[1]);

            List<Object[]> resultList = em.createQuery("select m.username, m.age  from Member m").getResultList();
            Object[] result = resultList.get(0);
            System.out.println("result[0] = " + result[0]);
            System.out.println("result[1] = " + result[1]);

            List<Object[]> resultList = em.createQuery("select m.username, m.age  from Member m").getResultList();
            Object[] result = resultList.get(0);
            System.out.println("result[0] = " + result[0]);
            System.out.println("result[1] = " + result[1]);
            System.out.println("resultList = " + resultList);


            List<MemberDTO> resultList = em.createQuery("select new org.example.jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
                    .getResultList();

            //필요한부분만 DTO로 만들어서 뽑을 수 있음 생성자 랑 순서 맞춰야 됨
            List<MemberDTO> resultList = em.createQuery("select new org.example.jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
                    .getResultList();

            MemberDTO memberDTO = resultList.get(0);
            System.out.println("memberDTO.getUsername() = " + memberDTO.getUsername());
            System.out.println("memberDTO.getAge() = " + memberDTO.getAge());

            //페이징 처리
            for(int i = 0; i < 100; i++){
                Member member = new Member();
                member.setUsername("member" + i);
                member.setAge(i);
                em.persist(member);
            }
            //페이징 처리 setFirstResult(), setMaxResults()로 공통으로 모든 DB처리 가능 이유로는 persistence.xml dialect 방언처리로
            //상황에 맞게 DB SQL을 전송함
            List<Member> result = em.createQuery("select m from Member m order by m.age desc", Member.class)
                    .setFirstResult(1)
                    .setMaxResults(10)
                    .getResultList();

            System.out.println("result = " + result.size());
            for (Member member1 : result) {
                System.out.println("member1 = " + member1);
            }

            //내부조인 inner join inner 생략 가능
            String query = "select m from Member m inner join m.team t";
            List<Member> result = em.createQuery(query, Member.class)
                    .getResultList();

            //외부조인 left join outer 생략 가능
            String query = "select m from Member m left outer join m.team t";
            List<Member> result = em.createQuery(query, Member.class)
                    .getResultList();

            //세타조인 관계 없는 데이터를 크로스해서 조인
            String query = "select m from Member m, Team t where m.username=t.name";
            List<Member> result = em.createQuery(query, Member.class)
                    .getResultList();

            //조인 조건문에 추가 할 수 있음 on절을 통해 Member 와 team을 조인 teamA인 것만 조인
            String query = "select m from Member m left join m.team t on t.name = 'teamA' ";
            List<Member> result = em.createQuery(query, Member.class)
                    .getResultList();

            //연관 관계 없는 엔티티 외부 조인
            //예) 회원의 이름과 팀의 이름이 같은 대상 외부 조인
            String query = "select m from Member m left join Team t on m.username = t.name";
            List<Member> result = em.createQuery(query, Member.class)
                    .getResultList();
            //연관 관계 없는 내부 조인
            String query = "select m from Member m join Team t on m.username = t.name";
            List<Member> result = em.createQuery(query, Member.class)
                    .getResultList();

            //enum인 경우 패키지를 다적어줘야함, 파라미터로 넣을경우 이렇게 안해도 됨
            String query = "select m.username, 'HELLO', TRUE  from Member m " +
                    "where m.type = org.example.jpql.MemberType.USER";
            List<Object[]> result = em.createQuery(query)
                    .getResultList();

            for (Object[] objects : result) {
                System.out.println("objects[0] = " + objects[0]);
                System.out.println("objects[1] = " + objects[1]);
                System.out.println("objects[2] = " + objects[2]);
            }

            //다형성 형태로 불러올 수도 있음 상속관계에서 Dtype을 챙겨 줌
            em.createQuery("select i from Item i where type(i) = BOOK", Item.class);

            //기본 CASE식
            String query =
                    "select " +
                            "case when m.age <= 10 then '학생요금'" +
                            "     when m.age <= 60 then '경로요금'" +
                            "     else '일반요금' end " +
                            "from Member m";
            List<String> result = em.createQuery(query, String.class).getResultList();

            for (String s : result) {
                System.out.println("s = " + s);
            }

            //COALESCE : 하나씩 조회해서 null이 아니면 반환
            String query = "select coalesce(m.username, '이름 없는 회원') from Member m ";
            List<String> result = em.createQuery(query, String.class).getResultList();

            for (String s : result) {
                System.out.println("s = " + s);
            }

            //nullif 해당 값과 설정된 값이 같으면 NULL반환
            String query = "select nullif(m.username, '관리자') from Member m ";
            List<String> result = em.createQuery(query, String.class).getResultList();

            for (String s : result) {
                System.out.println("s = " + s);
            }

            //concat등 함수들은 참조 할것
            String query = "select 'a' || 'b' from Member m ";
            List<String> result = em.createQuery(query, String.class).getResultList();

            for (String s : result) {
                System.out.println("s = " + s);
            }
            //만든 펑션 사용
            String query = "select function('group_concat', m.username) from Member m ";
            List<String> result = em.createQuery(query, String.class).getResultList();

            for (String s : result) {
                System.out.println("s = " + s);
            }
            //경로표현식으로 인한 탐색은 자제 묵시적 조인이 일어나서 JOIN이 원하지 않게 계속 이루어 질 수 있음
            //JOIN은 성능에도 영향을 끼치기 때문에 명시적 조인을 통해 직접 join을 작성하는 것을 권장
            String query = "select t.members  from Team t ";
            List result = em.createQuery(query, Collection.class).getResultList();

            for (Object o : result) {
                System.out.println("o = " + o);
            }

            //N+1 해결 방법으로는 1번의 호출시 다른테이블값의 N만큼 쿼리가 호출된다는 의미
            //지연로딩으로 인해 해당부분이 필요한 시점에 루프가 돌때 참조된 값 조회시
            //쿼리가 매번 날라가는 이슈가 발생함 이를 방지하기 위해 fetch를 붙여 패치조인으로
            //한번에 영속성 컨텍스트에 올리고 1차캐시로 값을 조회함 이방법을 사용시 프록시형태로 조회하지 않음

            String query = "select m From Member m join fetch m.team";

            List<Member> result = em.createQuery(query, Member.class).getResultList();

            for (Member member : result) {
                System.out.println("member = " + member.getUsername() + ", " + member.getTeam().getName());
                //회원1, 팀A(SQL)
                //회원2, 팀A(1차캐시)
                //회원3, 팀B(SQL)
            }
            //패치 조인
            //
            //일반적으로 연관관계는 즉시로딩으로 설정하지않고 지연 로딩으로 설정해야함
            //이유로는 필요치 않게 데이터들을 조인하여서 가져올 수 있는 이슈가 있기때문임
            //그렇지만 지연로딩으로 설정할 시 컬렉션 값을 로드 할시 N + 1 이슈가 발생함
            //N+1이란 쿼리 한번에 거기에 연관되어있는 Many수만큼 쿼리가 동작한다는 소리임
            //그렇기때문에 필요한부분에는 join fetch를 사용하여 한번에 즉시로딩으로 데이터를 조회하여
            //영속성 컨텍스트에 1차캐시로  데이터를 저장하면 N+1문제가 해결 될 수 있음

            //1대 다인 경우 데이터 뻥튀기기가 될 수 있기때문에 패치조인과 distinct를 붙여서 방지해야함
            //패치조인으로 인한 탐색은 전체 데이터를 객체그래프로 탐색하는데 초점을 둬야함 데이터를 줄이고 이런 행위를 하면 장애여부 증가
            // .setFirstResult()
            // .setMaxResults() 페이징 처리 지원 x
            //필요한 경우 다대일인 쿼리 형태로 바꿔서 페이징처리를 하던지 아니면 @OneToMany나 @ManyToMany에
            //@BatchSize()를 통해 id값으로 한번에 조회 하는 방법을 사용하던지
            //<property name="hibernate.default_batch_fetch_size" value=""/>를 xml에 처리하여 해결
            String query = "select distinct t From Team t join fetch t.members";

            List<Team> result = em.createQuery(query, Team.class)
                    .getResultList();

            for (Team team : result) {
                System.out.println("team = " + team.getName() + " " + team.getMembers().size());
                for(Member member : team.getMembers()){
                    System.out.println("member = " + member);
                }
            }

            String query = "select i from Item i where type(i) = 'BOOK'"; //Dtype조회
            String query1 = "select i from Item i where treat(i as BOOK).auther='shin'"; //이런형태로 부모에서 자식으로 다운캐스팅해서 사용 할 수 있음

            //JPA는 변수로 엔티티를 넣을수있음 넣으면 고정으로 식별자 key로 m.id로 조회 됨
            //외래키로 조회도 가능함
            String query = "select m From Member m where m = :member";

            Member findMember = em.createQuery(query, Member.class)
                    .setParameter("member", member1)
                    .getSingleResult();

            System.out.println("findMember = " + findMember);

            //named Query 쿼리를 xml이나 entity에 미리 생성해놓고 사용 할 수 있음
            //또한 좋은오류 컴파일할때 오류가 발생하여 클라이언트가 요청시 오류가 나는것을 미리 예방 할 수 있음
            List<Member> resultList = em.createNamedQuery("Member.findByUsername", Member.class)
                    .setParameter("username", "회원1")
                    .getResultList();

            for (Member member : resultList) {
                System.out.println("member = " + member);
            }

            //벌크연산은 한번에 업데이트하거나 삭제할때 사용됨
            //주의해야될점은 영속성 컨텍스트를 적용해주지 않기때문에 업데이트나 삭제처리후에
            //엔티티매니저를 clear 해줘야함 아니면 업데이트한 값이아니라 기존에 있는 값이 캐시에있어서 문제가 발생 할수있음
            //다른방법으로는 벌크연산을 최초에 실행하는 방법이 있음
            int resultCount = em.createQuery("update Member m set m.age = 20")
                    .executeUpdate();

            System.out.println("resultCount = " + resultCount);

            */
            em.flush();
            em.clear();

            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            tx.rollback();
        }finally {
            em.close();
        }

        emf.close();
    }



}
