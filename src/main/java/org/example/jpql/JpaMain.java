package org.example.jpql;

import javax.persistence.*;
import java.util.List;

public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            for(int i = 0; i < 100; i++){
                Member member = new Member();
                member.setUsername("member" + i);
                member.setAge(i);
                em.persist(member);
            }

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

            */

            List<Member> result = em.createQuery("select m from Member m order by m.age desc", Member.class)
                    .setFirstResult(1)
                    .setMaxResults(10)
                    .getResultList();

            System.out.println("result = " + result.size());
            for (Member member1 : result) {
                System.out.println("member1 = " + member1);
            }

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
